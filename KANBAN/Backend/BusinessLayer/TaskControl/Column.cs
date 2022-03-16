using System;
using System.Collections.Generic;
using System.Linq;
using DAL = IntroSE.Kanban.Backend.DataAccessLayer;

namespace IntroSE.Kanban.Backend.BusinessLayer.TaskControl
{
    class Column : IPersistentObject<DAL.Column>
    {
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        internal int host;
        internal int ord;
        private const int maxname = 15;
        private const int defLimit = 100;
        internal string name;
        internal List<Task> tasks;
        internal int limit;
        internal int size;
        internal bool fortests = false;//for testing
        public Column(int host, string name, int ord)
        {
            log.Info("creating new empty " + name + " column for " + host + ".");
            this.host = host;
            if (name == null || name == "" || name.Length > maxname) throw new Exception("illegal name.");
            this.name = name;
            if (ord < 0) throw new Exception("ordinal illegal.");
            this.ord = ord;
            tasks = new List<Task>();
            size = 0;
            limit = defLimit;
            DAL.Column Dcol = ToDalObject();
            Dcol.Add();
        }
        public Column()
        {
            tasks = new List<Task>();
        }
        public int GetSize()
        {
            return size;
        }
        public int GetOrd()
        {
            return ord;
        }
        public int GetLimit()
        {
            return limit;
        }
        public string GetName()
        {
            return name;
        }
        public virtual List<Task> GetAll()
        {
            log.Debug("returning all tasks");
            return tasks;
        }
        public virtual void SetOrd(int ord)
        {
            if (ord < 0) throw new Exception("ordinal illegal.");
            this.ord = ord;
            DAL.Column Dcol = ToDalObject();
            Dcol.UpdateOrd(ord);
        }
        public void SetLimit(int host, int limit) // set the limit of this column
        {
            if (host != this.host) throw new Exception("non host user tried to change column limit.");
            log.Info("changing task limit for column: " + name + " in " + host + " from: " + this.limit + " to: " + limit + ".");
            if (limit < size | limit < 1)
            {
                log.Warn("limit cannot be lower than current amount of tasks. limit not changed.");
                throw new Exception("limit cannot be lower than current amount of tasks. limit not changed.");
            }
            this.limit = limit;
            DAL.Column Dcol = ToDalObject();
            Dcol.UpdateLimit(limit);
        }
        public virtual void AddTasks(List<Task> ts) // append a list of tasks to the end of this column
        {
            fortests = true;
            if (size + ts.Count() > limit)
            {
                log.Warn("task limit reached, tasks not added.");
                throw new Exception("task limit reached, tasks not added.");
            }
            foreach (Task t in ts) t.EditColumn(name);
            tasks.AddRange(ts);
            size += ts.Count;
        }
        public virtual void AddTask(Task task) // add a new task to this column
        {
            log.Debug("adding task: #" + task.GetID() + " title: " + task.GetTitle() + " to column: " + name + " in " + host + ".");
            if (limit <= size)
            {
                log.Warn("task limit reached, task not added.");
                throw new Exception("task limit reached, task not added.");
            }
            if (ord == 0) task.Insert();
            task.EditColumn(name);
            tasks.Add(task);
            size++;
        }
        public virtual Task DeleteTask(string email, Task task) // delete a task from this column (if exists) and return it
        {
            task.CheckAssig(email);
            log.Debug("removing task: #" + task.GetID() + " title: " + task.GetTitle() + " from column: " + name + " in " + host + ".");
            if (tasks.Remove(task))
            {
                size--;
                return task;
            }
            log.Info("task does not exist in " + name + " column.");
            return null;
        }
        public Task GetTask(int ID) // get a task from this column by ID
        {
            log.Debug("retrieving task with ID: " + ID + " from column: " + name + " in " + host + ".");
            foreach (Task task in tasks)
            {
                if (task.GetID() == ID)
                {
                    return task;
                }
            }
            log.Info("task does not exist in " + name + " column.");
            return null;
        }
        public DAL.Column ToDalObject() // convert this column to a DataAccessLayer object
        {
            log.Debug("column " + name + " converting to DAL obj in " + host + ".");
            try
            {
                List<DAL.Task> Dtasks = new List<DAL.Task>();
                foreach (Task t in tasks)
                {
                    Dtasks.Add(t.ToDalObject());
                }
                DAL.Column c = new DAL.Column(host, name, ord, limit);
                return c;
            }
            catch (Exception e)
            {
                log.Error("issue converting column BL object to column DAL object due to " + e.Message);
                throw e;
            }
        }
        public void FromDalObject(DAL.Column DalObj) // convert a DataAccessLayer object to a BuisnessLayer column and set this to corresponding values
        {
            log.Debug("column " + DalObj.Cname + " converting from DAL obj in " + DalObj.Email + ".");
            try
            {
                host = (int)DalObj.Host;
                name = DalObj.Cname;
                ord = (int)DalObj.Ord;
                limit = (int)DalObj.Limit;
                log.Debug(host + " " + name + " " + ord + " " + limit);
                DalObj.LoadTasks();
                foreach (DAL.Task t in DalObj.GetTasks()) // convert each task in column individually
                {
                    Task BT = new Task();
                    BT.FromDalObject(t);
                    log.Debug("made task: " + BT.GetID() + " with title: " + BT.GetTitle());
                    tasks.Add(BT);
                }
                size = tasks.Count();
            }
            catch (Exception e)
            {
                log.Error("issue converting column DAL object to column BL object due to " + e.Message);
                throw e;
            }
        }
        public void EditTitle(int ID, string title, string assig) // update title of this task
        {
            Task t = GetTask(ID);
            if (t == null)
            {
                throw new Exception("task does not exist in this columm.");
            }
            t.EditTitle(assig, title);
        }
        public void EditDesc(int ID, string desc, string assig)// update description of this task
        {
            Task t = GetTask(ID);
            if (t == null)
            {
                throw new Exception("task does not exist in this columm.");
            }
            t.EditDesc(assig, desc);
        }
        public void EditDue(int ID, DateTime due, string assig)// update due date of this task
        {
            Task t = GetTask(ID);
            if (t == null)
            {
                throw new Exception("task does not exist in this columm.");
            }
            t.EditDue(assig, due);
        }
        public virtual void Delete(int host)
        {
            if (host != this.host) throw new Exception("non host user tried to delete a column.");
            DAL.Column c = new DAL.Column(host, name);
            c.Delete();
        }
        public void ChangeColumnName(int host, string newName)
        {
            log.Debug("in column with " + host + " and " + this.host);
            if (host != this.host | newName.Length > maxname) throw new Exception("non host user tried to change column name.");
            foreach (Task task in tasks) task.EditColumn(newName);
            DAL.Column t = ToDalObject();
            t.UpdateName(newName);
            name = newName;
        }
    }
}