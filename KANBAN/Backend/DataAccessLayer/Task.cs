using IntroSE.Kanban.Backend.DataAccessLayer.DALControllers;
using System;
using System.Collections.Generic;

namespace IntroSE.Kanban.Backend.DataAccessLayer
{
    class Task : DalObject<Task>
    {
        public const string HostAtt = DB._hostcolumn;
        public const string IDAtt = DB._tidcolumn;
        public const string EmailAtt = DB._assigneecolumn;
        public const string ColumnAtt = DB._columnnamecolumn;
        public const string TitleAtt = DB._titlecolumn;
        public const string DescAtt = DB._desccolumn;
        public const string DueAtt = DB._duedatecolumn;
        public const string createAtt = DB._createcolumn;
        public long ID { get; }
        public string Cname { get; set; }
        public string Title { get; set; }
        public string Desc { get; set; }
        public long Due { get; set; }
        public long Create { get; set; }
        public long HostID { get; set; }
        public Task(long HostID, long ID, string Email, string Cname, string Title, string Desc, long Due, long Cre) : base(new TaskCtrl())
        {
            this.Email = Email;
            this.ID = ID;
            this.Cname = Cname;
            this.Title = Title;
            this.Desc = Desc;
            this.Due = Due;
            this.Create = Cre;
            this.HostID = HostID;
        }
        protected override string MakeFilter()
        {
            return $"WHERE {HostAtt}={HostID} AND {IDAtt}={ID}";
        }
        public Task() : base(new TaskCtrl())
        {
            log.Debug("creating DAL task");
        }
        public List<Task> GetAllTasks(long host, string Cname)
        {
            List<Task> c = controller.Select($"WHERE {HostAtt}={host} AND {ColumnAtt}='{Cname}'");
            return c;
        }
        public void UpdateTitle(string t)
        {
            if (!controller.Update(MakeFilter(), TitleAtt, t))
            {
                log.Error("fail to updata the limit for task " + Cname + " on email " + Email);
                throw new Exception("fail to updata the limit for task " + Cname + " on email " + Email);
            }
        }
        public void UpdateDesc(string d)
        {
            if (!controller.Update(MakeFilter(), DescAtt, d))
            {
                log.Error("fail to updata the ordinal for task " + Cname + " on email " + Email);
                throw new Exception("fail to updata the ordinal for task " + Cname + " on email " + Email);
            }
        }
        public void UpdateColumn(string c)
        {
            if (!controller.Update(MakeFilter(), ColumnAtt, c))
            {
                log.Error("fail to update the name for task " + Cname + " on email " + Email);
                throw new Exception("fail to updata the name for task " + Cname + " on email " + Email);
            }
        }

        internal void UpdateAssignee(string assig)
        {
            if (!controller.Update(MakeFilter(), EmailAtt, assig))
            {
                log.Error("fail to update the name for task " + assig + " on email " + Email);
                throw new Exception("fail to updata the name for task " + assig + " on email " + Email);
            }
        }

        public void UpdateDue(long d)
        {
            if (!controller.Update(MakeFilter(), DueAtt, d))
            {
                log.Error("fail to updata the name for task " + Cname + " on email " + Email);
                throw new Exception("fail to updata the name for task " + Cname + " on email " + Email);
            }
        }
        public void Add()
        {
            if (!controller.Insert(this))
            {
                log.Error("fail to add new task for email " + Email);
                throw new Exception("fail to add new task for email " + Email);
            }
        }
    }
}
