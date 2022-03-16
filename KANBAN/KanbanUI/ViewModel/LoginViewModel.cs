using KanbanUI.Model;
using System;

namespace KanbanUI.ViewModel
{
    public class LoginViewModel : NotifiableObject
    {
        public BackendController Controller { get; private set; }
        private string _email;
        public string Email { get => _email; set { _email = value; RaisePropertyChanged("Email"); } }
        private string _password;
        public string Password { get => _password; set { _password = value; RaisePropertyChanged("Password"); } }
        private string _massage;
        public string Message { get => _massage; set { _massage = value; RaisePropertyChanged("Message"); } }

        public LoginViewModel() // empty constructor to run at statup
        {
            Controller = new BackendController();
        }
        public LoginViewModel(BackendController b) // constructor to recycle controller at logout
        {
            Controller = b;
        }
        public UserModel Login()
        {
            Message = "";
            try
            {
                return Controller.Login(_email, _password);
            }
            catch (Exception e)
            {
                Message = e.Message;
                return null;
            }
        }

        // reset button to delete all data. not part of final build but in code for testing

        //public void Reset()
        //{
        //    Message = "";
        //    try
        //    {
        //        Controller.Reset();
        //        Message = "all data successfully deleted.";
        //    }
        //    catch (Exception e)
        //    {
        //        Message = e.Message;
        //    }
        //}
    }
}
