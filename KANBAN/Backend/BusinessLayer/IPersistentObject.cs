using DAL = IntroSE.Kanban.Backend.DataAccessLayer;

namespace IntroSE.Kanban.Backend.BusinessLayer
{
    interface IPersistentObject<T> where T : DAL.DalObject<T>
    {
        T ToDalObject();
        void FromDalObject(T DalObj);
    }
}
