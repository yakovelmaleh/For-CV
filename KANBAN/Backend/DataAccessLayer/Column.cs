using IntroSE.Kanban.Backend.DataAccessLayer.DALControllers;
using System;
using System.Collections.Generic;
using System.Linq;

namespace IntroSE.Kanban.Backend.DataAccessLayer
{
    class Column : DalObject<Column>
    {
        public const string HostAtt = DB._hostcolumn;
        public const string NameAtt = DB._columnnamecolumn;
        public const string OrdAtt = DB._ordinalcolumn;
        public const string LimitAtt = DB._limitcolumn;
        private List<Task> tasks;
        public long Host { get; set; }
        public string Cname { get; set; }
        public long Ord { get; set; }
        public long Limit { get; set; }
        public Column(long Host, string Cname, long Ord, long Limit) : base(new ColumnCtrl())
        {
            this.Host = Host;
            this.Cname = Cname;
            this.Ord = Ord;
            this.Limit = Limit;
            tasks = new List<Task>();
        }
        public Column(long Host, string Cname) : base(new ColumnCtrl()) //filter only constructor for loading a single column
        {
            this.Host = Host;
            this.Cname = Cname;
        }

        protected override string MakeFilter()
        {
            return $"WHERE {HostAtt}={Host} AND {NameAtt}='{Cname}'";
        }

        public Column() : base(new ColumnCtrl()) { } //empty constructor for loading all column data
        public List<Column> GetAllColumns(int Host)
        {
            List<Column> output = controller.Select($"WHERE {HostAtt}={Host}");
            foreach (Column c in output)
            {
                c.LoadTasks();
                log.Debug("loaded tasks for column: " + c.Cname + " " + c.GetTasks().Count());
            }
            log.Debug("columns loaded for " + Host + " with size: " + output.Count());
            return output;
        }
        public void LoadTasks()
        {
            Task temp = new Task();
            tasks = temp.GetAllTasks(Host, Cname);
        }
        public List<Task> GetTasks() { return tasks; }
        public void UpdateLimit(long limit)
        {
            if (!controller.Update(MakeFilter(), LimitAtt, limit))
            {
                log.Error("failed to update the limit for column " + Cname + " on email " + Email);
                throw new Exception("failed to update the limit for column " + Cname + " on email " + Email);
            }
        }
        public void UpdateName(string name)
        {
            if (!controller.Update(MakeFilter(), NameAtt, name))
            {
                log.Error("failed to update the name for column " + Cname + " on email " + Email);
                throw new Exception("failed to name the limit for column " + Cname + " on email " + Email);
            }
        }
        public void UpdateOrd(long ord)
        {
            if (!controller.Update(MakeFilter(), OrdAtt, ord))
            {
                log.Error("failed to update the ordinal for column " + Cname + " on email " + Email);
                throw new Exception("failed to update the ordinal for column " + Cname + " on email " + Email);
            }
        }
        public void Add()
        {

            if (!controller.Insert(this))
            {
                log.Error("failed to add new column for email " + Email);
                throw new Exception("failed to add new column for email " + Email);
            }
        }
        public void Delete()
        {

            if (!controller.Delete(MakeFilter()))
            {
                log.Error("failed to delete a column from email " + Email);
                throw new Exception("failed to delete a column from email " + Email);
            }
        }
    }
}
