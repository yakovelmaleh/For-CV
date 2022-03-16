using IntroSE.Kanban.Backend.DataAccessLayer.DALControllers;

namespace IntroSE.Kanban.Backend.DataAccessLayer
{
    public abstract class DalObject<T> where T : DalObject<T>
    {
        protected static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        protected DALCtrl<T> controller;

        public string Email { get; set; } = "";
        protected DalObject(DALCtrl<T> controller)
        {
            this.controller = controller;
        }
        protected abstract string MakeFilter();
    }
}
