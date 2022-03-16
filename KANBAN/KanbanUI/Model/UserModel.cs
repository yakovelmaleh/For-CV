using System;

namespace KanbanUI.Model
{
    public class UserModel : NotifiableModelObject //basically a user data placeholder
    {
        public string Email { get; set; }

        public UserModel(BackendController controller, string email) : base(controller)
        {
            this.Email = email;
        }

        internal string Logout()
        {
            try
            {
                Controller.Logout(Email);
                return null;
            }
            catch (Exception e)
            {
                return e.Message;
            }
        }
    }
}
