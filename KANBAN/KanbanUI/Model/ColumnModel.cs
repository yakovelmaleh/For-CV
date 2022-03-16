using KanbanUI.Utils;
using System;
using System.Collections.ObjectModel;

namespace KanbanUI.Model
{
    public class ColumnModel : NotifiableModelObject
    {
        public int Index { get => _index; set { _index = value; RaisePropertyChanged("Index"); } }
        private int _index;
        private string _name;
        private readonly string email;
        public bool IsSorted { get; set; }
        public string ColumnName
        {
            get => _name; set
            {
                if (Changename(value)) // run changename login in backend before changing in presentation
                {
                    _name = value;
                }
                RaisePropertyChanged("ColumnName");
            }
        }
        private int _limit;
        public TaskCommand DeleteTaskClick { get; set; } // direct binding to model since no outside resources are required
        public string ColumnLimit
        {
            get => _limit.ToString(); set
            {
                if (ChangeLimit(value))// run changelimit login in backend before changing in presentation
                {
                    _limit = Int32.Parse(value); // since all presentation input is string parse to int
                    RaisePropertyChanged("ColumnLimit");
                }
            }
        }

        internal void Reload() // reload all column data for functions that change too much in one go to replicate in presentation
        {
            Tasks = Controller.GetColumn(email, ColumnName, Index).Item3;
            Sort();
            RaisePropertyChanged("tasks");
        }

        internal void Sort()// sort tasks by due date
        {
            if (IsSorted)
            {
                for (int i = 0; i < Tasks.Count; i++)
                {
                    var a = Tasks[i];
                    for (int j = i + 1; j < Tasks.Count; j++)
                    {
                        if (DateTime.Parse(a.Due) > DateTime.Parse(Tasks[j].Due))
                        {
                            Tasks[i] = Tasks[j];
                            Tasks[j] = a;
                            a = Tasks[i];
                        }
                    }
                }
                IsSorted = true;
                RaisePropertyChanged("tasks");
            }
        }
        private void OnDeleteTaskClick(TaskModel p)
        {
            try
            {
                Controller.DeleteTask(email, Index, p.ID);
                Tasks.Remove(p);
                RaisePropertyChanged("tasks");
            }
            catch
            {
            }
        }


        public ObservableCollection<TaskModel> Tasks { get; set; }


        public ColumnModel(BackendController c, string Name, string email, int n) : base(c)
        {
            Tuple<string, int, ObservableCollection<TaskModel>> col = Controller.GetColumn(email, Name, n);
            _name = col.Item1;
            _limit = col.Item2;
            Tasks = col.Item3;
            this.email = email;
            Index = n;
            IsSorted = false;
            DeleteTaskClick = new TaskCommand(OnDeleteTaskClick);
        }

        internal void Filter(string filter)
        {
            foreach (TaskModel t in Tasks)
            {
                t.Filter(filter);
            }
        }

        private bool Changename(string newname)
        {
            try
            {
                Controller.ChangeColumnName(Index, newname, email);
                return true;
            }
            catch { return false; }
        }

        private bool ChangeLimit(string newLimit)
        {
            try
            {
                Controller.ChangeColumnLimit(Index, int.Parse(newLimit), email);
                return true;
            }
            catch { return false; }
        }
    }
}
