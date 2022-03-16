using IntroSE.Kanban.Backend.BusinessLayer;
using System;
namespace IntroSE.Kanban.Backend.ServiceLayer.SubService
{
    class UserService
    {
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        private readonly BusinessLayer.UserControl.UserController uc;

        public UserService(UBlink u)
        {
            uc = new BusinessLayer.UserControl.UserController(u);
        }
        public Response<User> Login(string email, string password) // login an existing user
        {
            try
            {
                log.Info("logging in to user " + email + ".");
                uc.Login(email, password);
                return new Response<User>(new User(uc.Get_active().Getemail(), uc.Get_active().Getnickname()));
            }
            catch (Exception e)
            {
                return new Response<User>(e.Message);
            }
        }
        public Response Register(string email, string password, string nickname) // register a new user
        {
            try
            {
                log.Info("registering user " + email + ".");
                uc.Register(email, password, nickname);
                return new Response();
            }
            catch (Exception e)
            {
                log.Info("register failed. due to " + e.Message); // can fail due to many reasons, added catch all.
                return new Response(e.Message);
            }
        }
        public Response Register(string email, string password, string nickname, string emailHost)
        {
            try
            {
                log.Info("registering user " + email + ".");
                uc.Register(email, password, nickname, emailHost);
                return new Response();
            }
            catch (Exception e)
            {
                log.Info("register failed. due to " + e.Message); // can fail due to many reasons, added catch all.
                return new Response(e.Message);
            }
        }
        public Response Drop() //drop all user data 
        {
            try
            {
                log.Info("dropping all user data.");
                uc.Drop();
                return new Response();
            }
            catch (Exception e)
            {
                return new Response(e.Message);
            }
        }
        public Response Logout(string email) // logout active user
        {
            try
            {
                log.Info("logging out user " + email + ".");
                uc.Logout(email);
                return new Response();
            }
            catch (Exception e)
            {
                log.Info("login failed."); // can fail due to many reasons, added catch all.
                return new Response(e.Message);
            }
        }
        public Response LoadData() // load all user data
        {
            try
            {
                log.Info("attempting to load user list.");
                uc.LoadData();
                return new Response();
            }
            catch (Exception e) { return new Response(e.Message); }
        }
    }
}
