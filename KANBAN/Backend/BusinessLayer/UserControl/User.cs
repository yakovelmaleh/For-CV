using System;
using DAL = IntroSE.Kanban.Backend.DataAccessLayer;

namespace IntroSE.Kanban.Backend.BusinessLayer.UserControl
{
    class User : IPersistentObject<DAL.User>
    {
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        private String email;
        private String password;
        private String nickname;
        private int emailHost = 0;
        private int UID;
        private DAL.User DU;

        public User() { }
        public User(string email, string password, string nickname, int emailHost) // new user creation (register) constructor
        {
            this.email = email;
            this.password = password;
            this.nickname = nickname;
            this.emailHost = emailHost;
        }
        public string Getemail()
        {
            return email;
        }
        public bool IsMatchEmailHost() { return UID.Equals(emailHost); }
        public bool IsMatchPassword(string password)
        {
            return this.password.Equals(password);
        }
        public bool IsMatchEmail(string email)
        {
            return this.email.Equals(email);
        }
        public string Getnickname()
        {
            return nickname;
        }
        public DAL.User ToDalObject() // converts this object to a DataAccessLayer object
        {
            log.Debug("converting user to DAL obj for " + email + ".");
            return new DAL.User(UID, email, password, nickname, emailHost);
        }

        public void FromDalObject(DAL.User DalObj) // converts a DataAccessLayer object to an object of this type and sets this to the corresponding values
        {
            try
            {
                log.Debug("converting user from DAL obj for " + DalObj.Email + ".");
                this.email = DalObj.Email;
                this.password = DalObj.password;
                this.nickname = DalObj.Nickname;
                this.emailHost = (int)DalObj.EmailHost;
                this.UID = (int)DalObj.UID;
            }
            catch (Exception e)
            {
                log.Error("issue converting user DAL object to user BL object due to " + e.Message);
                throw e;
            }
        }
        public void Insert()
        {
            DU = ToDalObject();
            DU.Insert();
        }

        internal void UpdateHost(int i)
        {
            DU.UpdateHost(i);
        }
    }
}
