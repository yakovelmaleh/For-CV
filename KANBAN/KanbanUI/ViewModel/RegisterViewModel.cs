using System;

namespace KanbanUI.ViewModel
{

    class RegisterViewModel : NotifiableObject // register has only one method so a full model seemed like overkill
    {
        readonly BackendController controller;
        private string _email;
        public string Email { get => _email; set { _email = value; RaisePropertyChanged("Email"); } }
        private string _password;
        public string Password { get => _password; set { _password = value; RaisePropertyChanged("Password"); } }
        private string _nickname;
        public string Nickname { get => _nickname; set { _nickname = value; RaisePropertyChanged("Nickname"); } }
        private string _host;
        public string Host { get => _host; set { _host = value; RaisePropertyChanged("Host"); } }

        private string _massage;
        public string Message { get => _massage; set { _massage = value; RaisePropertyChanged("Message"); } }
        public RegisterViewModel(BackendController controller)
        {
            this.controller = controller;
        }

        public void Register()
        {
            Message = "";
            try
            {
                controller.Register(Email, Password, Nickname, Host);
                Message = "Register successful.";
                Email = null;
                Password = null;
                Nickname = null;
                Host = null;
            }
            catch (Exception e)
            {
                Message = e.Message;
            }
        }
    }
}
