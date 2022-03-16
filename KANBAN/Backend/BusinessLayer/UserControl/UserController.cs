using System;
using System.Collections.Generic;
using System.Globalization;
using System.Text.RegularExpressions;
using DAL = IntroSE.Kanban.Backend.DataAccessLayer;
using DC = IntroSE.Kanban.Backend.DataAccessLayer.DALControllers;

namespace IntroSE.Kanban.Backend.BusinessLayer.UserControl
{
    class UserController
    {
        private const int nothost = -1;
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        private const int MaxLength = 25;
        private const int MinLength = 5;
        private User ActiveUser;
        private List<User> list;
        private readonly UBlink lnk;
        private DC.UserCtrl Duc;
        private User NU;

        public UserController(UBlink u)
        {
            lnk = u;
            log.Debug("createing user controller.");
            this.ActiveUser = null;
            Duc = new DC.UserCtrl();
        }
        public User Get_active() { return ActiveUser; }
        public void Register(string email, string password, string nickname) // register a new user
        {
            log.Debug("Email: " + email + " Password: " + password + " nickname: " + nickname);
            NullCheck(email, password, nickname);
            email = email.ToLower();
            CheckEmail(email);
            CheckUser(email);
            CheckPassword(password);
            Save(email, password, nickname, nothost);
            int host = FindID(email);
            log.Debug("register values are legal.");
            lnk.Lastemail = email;
            lnk.LastId = host;
            UpdateHost(host);
        }

        public void Register(string email, string password, string nickname, string emailHost)
        {
            log.Debug("Email: " + email + " Password: " + password + " nickname: " + nickname + " Host: " + emailHost);
            int check = FindID(emailHost);
            NullCheck(email, password, nickname, emailHost);
            email = email.ToLower();
            emailHost = emailHost.ToLower();
            CheckEmail(email);
            CheckUser(email);
            CheckPassword(password);
            Save(email, password, nickname, check);
            lnk.Lastemail = email;
            lnk.LastId = FindID(email);
            lnk.HostId = FindID(emailHost);
            log.Debug("register values are legal.");
        }
        private void UpdateHost(int i)
        {
            NU.UpdateHost(i);
        }
        private int FindID(string email)
        {
            Duc = new DC.UserCtrl();
            return Duc.FindLastID(email);
        }

        private void NullCheck(params object[] s) // checks if any of the given parameters are null or empty
        {
            foreach (object check in s)
            {
                if (check == null)
                {
                    log.Warn("entered at least one null value.");
                    throw new Exception("must enter non null values.");
                }
                else if ((string)check == "")
                {
                    log.Warn("entered at least one empty value.");
                    throw new Exception("must enter non empty values.");
                }
            }
        }
        private void CheckPassword(string password) // check if a password matches the requirements given
        {
            var hasNumber = new Regex(@"[0-9]+");
            var hasUpperChar = new Regex(@"[A-Z]+");
            var hasLowerChar = new Regex(@"[a-z]+");
            if (!hasNumber.IsMatch(password) | !hasUpperChar.IsMatch(password) | password.Length < MinLength | password.Length > MaxLength | !hasLowerChar.IsMatch(password))
            {
                log.Warn("password too weak. must include at least one uppercase letter, one lowercase letter and a number and be between 5 and 25 characters.");
                throw new Exception("must include at least one uppercase letter, one lowercase letter and a number and be between 5 and 25 characters.");
            }
        }
        private void Save(string email, string password, string nickname, int IDHost) // saves newly registered user
        {
            NU = new User(email, password, nickname, IDHost);
            NU.Insert();
            log.Info("user created for " + NU.Getemail());
            list.Add(NU);
        }
        public void Login(string email, string password) // login an existing user
        {
            if (ActiveUser != null)
            { // cant log in if a user is already logged in
                log.Warn("a login was attempted while a user is already logged in.");
                throw new Exception("a user is already logged in.");
            }
            NullCheck(email, password);
            email = email.ToLower();
            CheckEmail(email);
            foreach (User u in list) // run on user list to find correct user to login
            {
                if (u.IsMatchEmail(email))
                {
                    log.Debug("email " + email + " exists in the system.");
                    if (u.IsMatchPassword(password))
                    {
                        log.Debug("given password matches.");
                        ActiveUser = u;
                        log.Info(ActiveUser.Getemail() + " has successfully logged in.");
                    }
                    else
                    {
                        log.Warn(u.Getemail() + " tried to login with incorrect password.");
                        throw new Exception("invaild password.");
                    }
                }
            }
            if (ActiveUser == null)
            {
                log.Warn("user not yet registered.");
                throw new Exception(email + " user not yet registered.");
            }
        }
        public void Logout(string email) // log out active user
        {
            if (ActiveUser == null) // if no active user no one can log out
            {
                log.Warn("no user logged in. logout failed.");
                throw new Exception("no user logged in.");
            }
            email = email.ToLower();
            if (!email.Equals(ActiveUser.Getemail()))
            {
                log.Warn("a user that is not logged in attempted to log out. logout failed.");
                throw new Exception("given email is invalid.");
            }
            else
            {
                log.Debug(ActiveUser.Getemail() + " logged out.");
                ActiveUser = null;
            }
        }
        private void CheckUser(string email) // checks that an email is not taken upon registration
        {
            foreach (User u in list)
            {
                if (u.Getemail().Equals(email))
                {
                    log.Warn("attempted to register with a taken email.");
                    throw new Exception("this email is already taken.");
                }
            }
        }
        public void LoadData() // load userlist from json files
        {
            try
            {
                if (lnk.Load) { log.Warn("Data already loaded."); throw new Exception("Data already loaded."); }
                list = new List<User>();
                DC.UserCtrl DUC = new DC.UserCtrl();
                foreach (DAL.User run in DUC.Select(""))
                {
                    User u = new User();
                    u.FromDalObject(run);
                    list.Add(u);
                }
            }
            catch (Exception e)
            {
                log.Error("faild to load data");
                throw new Exception("faild to load data: " + e.Message);
            }
        }
        private void CheckEmail(string s) // check that email adress matches standard email format
        {
            try
            {

                if (string.IsNullOrWhiteSpace(s)) // check that email is not null and contains no spaces
                    throw new Exception("email adress invalid.");

                string DomainMapper(Match match) // creates a regex map for replace function
                {
                    var idn = new IdnMapping();
                    var domainName = idn.GetAscii(match.Groups[2].Value);
                    return match.Groups[1].Value + domainName;
                }
                s = Regex.Replace(s, @"(@)(.+)$", DomainMapper, RegexOptions.None, TimeSpan.FromMilliseconds(200));
                if (!Regex.IsMatch(s,
                    @"^(?("")("".+?(?<!\\)""@)|(([0-9a-z]((\.(?!\.))|[-!#\$%&'\*\+/=\?\^`\{\}\|~\w])*)(?<=[0-9a-z])@))" +
                    @"(?(\[)(\[(\d{1,3}\.){3}\d{1,3}\])|(([0-9a-z][-0-9a-z]*[0-9a-z]*\.)+[a-z0-9][\-a-z0-9]{0,22}[a-z0-9]))$", //stackoverflow regex
                    RegexOptions.IgnoreCase, TimeSpan.FromMilliseconds(250)))
                    throw new Exception("email adress invalid.");
            }
            catch (Exception)
            {
                log.Warn("email is invalid.");
                throw new Exception("email adress invalid.");
            }
        }
        public void Drop()
        {
            if (ActiveUser != null)
            {
                log.Error("can't drop user data while a user is logged in.");
                throw new Exception("can't drop user data while a user is logged in.");
            }
            list = new List<User>();
        }
    }
}
