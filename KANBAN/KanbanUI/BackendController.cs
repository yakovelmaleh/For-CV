using IntroSE.Kanban.Backend.ServiceLayer;
using KanbanUI.Model;
using System;
using System.Collections.ObjectModel;

namespace KanbanUI
{
    public class BackendController // link to backend
    {
        private readonly Service s;
        public BackendController()
        {
            s = new Service();
        }

        public Tuple<string, int, ObservableCollection<TaskModel>> GetColumn(string email, string name, int n)
        {
            // returns tuple to avoid multiple calls to backend or alternatively saving data in the controller
            Response<Column> res = s.GetColumn(email, name);
            IsErr(res);
            ObservableCollection<TaskModel> tasks = new ObservableCollection<TaskModel>();
            foreach (IntroSE.Kanban.Backend.ServiceLayer.Task t in res.Value.Tasks)
            {
                tasks.Add(TaskToModel(t, email, n));
            }
            return Tuple.Create(res.Value.Name, res.Value.Limit, tasks);
        }

        internal void DeleteColumn(string email, int index)
        {
            Response res = s.RemoveColumn(email, index);
            IsErr(res);
        }

        internal void AddTask(string email, string title, string desc, DateTime due)
        {
            Response res = s.AddTask(email, title, desc, due);
            IsErr(res);
        }

        internal void Logout(string email)
        {
            Response res = s.Logout(email);
            IsErr(res);
        }

        private TaskModel TaskToModel(IntroSE.Kanban.Backend.ServiceLayer.Task t, string email, int n)
        {
            return new TaskModel(this, t.Id, t.emailAssignee, t.Title, t.Description, t.DueDate, t.CreationTime, n, email);
        }

        internal void EditDue(string email, int columnIndex, int ID, DateTime due)
        {
            Response res = s.UpdateTaskDueDate(email, columnIndex, ID, due);
            IsErr(res);
        }

        internal void EditAssignee(string email, int columnIndex, int ID, string assignee)
        {
            Response res = s.AssignTask(email, columnIndex, ID, assignee);
            IsErr(res);
        }

        internal void AdvanceTask(string email, int columnIndex, int ID)
        {
            Response res = s.AdvanceTask(email, columnIndex, ID);
            IsErr(res);
        }

        internal void DeleteTask(string email, int columnIndex, int ID)
        {
            Response res = s.DeleteTask(email, columnIndex, ID);
            IsErr(res);
        }

        internal void EditDesc(string email, int columnIndex, int ID, string desc)
        {
            Response res = s.UpdateTaskDescription(email, columnIndex, ID, desc);
            IsErr(res);
        }

        internal void EditTitle(string email, int columnIndex, int ID, string title)
        {
            Response res = s.UpdateTaskTitle(email, columnIndex, ID, title);
            IsErr(res);
        }

        internal ColumnModel AddColumn(string email, int index, string name)
        {
            Response<Column> res = s.AddColumn(email, index, name);
            IsErr(res);
            return new ColumnModel(this, res.Value.Name, email, index);

        }

        public Tuple<string, ObservableCollection<ColumnModel>> GetBoard(UserModel um)
        {
            // returns tuple to avoid multiple calls to backend or alternatively saving data in the controller
            Response<Board> res = s.GetBoard(um.Email);
            IsErr(res);
            ObservableCollection<ColumnModel> temp = new ObservableCollection<ColumnModel>();
            int i = 0;
            foreach (string s in res.Value.ColumnsNames)
            {
                temp.Add(ColumnToModel(s, um.Email, i));
                i++;
            }
            return Tuple.Create(res.Value.emailCreator, temp);
        }
        private ColumnModel ColumnToModel(string s, string email, int n)
        {
            return new ColumnModel(this, s, email, n);
        }

        public UserModel Login(string email, string password)
        {
            Response<User> res = s.Login(email, password);
            IsErr(res);
            return new UserModel(this, res.Value.Email);

        }
        public void Register(string email, string password, string nickname, string host)
        {
            Response res;
            if (string.IsNullOrEmpty(host)) // service has 2 register overloads to operate with or without a host
            {
                res = s.Register(email, password, nickname);
            }
            else
            {
                res = s.Register(email, password, nickname, host);
            }
            if (res.ErrorOccured) throw new Exception(res.ErrorMessage);
        }

        // reset function to delete all data during testing. not part of final build

        //public void Reset()
        //{
        //    Response res = s.DeleteData();
        //    if (res.ErrorOccured) throw new Exception(res.ErrorMessage);
        //}

        private void IsErr(Response res) // backend error detection
        {
            if (res.ErrorOccured)
            {
                throw new Exception(res.ErrorMessage);
            }
        }
        public void ChangeColumnName(int index, string newname, string email)
        {
            Response res = s.ChangeColumnName(email, index, newname);
            IsErr(res);
        }
        public void ChangeColumnLimit(int index, int newLimit, string email)
        {
            Response res = s.LimitColumnTasks(email, index, newLimit);
            IsErr(res);
        }
        public void MoveLeft(string email, int ind)
        {
            Response res = s.MoveColumnLeft(email, ind);
            IsErr(res);
        }
        public void MoveRight(string email, int ind)
        {
            Response res = s.MoveColumnRight(email, ind);
            IsErr(res);
        }
    }
}
