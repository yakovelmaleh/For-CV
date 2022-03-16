using System;
using System.Collections.ObjectModel;

namespace KanbanUI.Model
{
    public class BoardModel : NotifiableModelObject
    {
        public bool IsSorted { get; set; }
        public UserModel UM; 
        public string host;
        public ObservableCollection<ColumnModel> Columns { get; set; }
        public BoardModel(UserModel um) : base(um.Controller)
        {
            UM = um;
            Tuple<string, ObservableCollection<ColumnModel>> board = Controller.GetBoard(um);
            host = board.Item1;
            Columns = board.Item2;
            IsSorted = false;
        }

        internal void ReLoad() // reload all board data for functions that change too much in one go to replicate in presentation
        {
            Columns = Controller.GetBoard(UM).Item2;
            Sort();
            RaisePropertyChanged("columns");
        }
        internal void Sort() // sort tasks by due date
        {
            if (IsSorted)
            {
                foreach (ColumnModel cm in Columns)
                {
                    cm.IsSorted = true;
                    cm.Sort();
                }
                RaisePropertyChanged("columns");
                IsSorted = true;
            }
        }
        internal void Delete(ColumnModel p)
        {
            Controller.DeleteColumn(UM.Email, p.Index);
            ReLoad();
        }

        internal void MoveRight(ColumnModel p)
        {
            Controller.MoveRight(UM.Email, p.Index);
            ReLoad();
        }
        internal void MoveLeft(ColumnModel p)
        {
            Controller.MoveLeft(UM.Email, p.Index);
            ReLoad();
        }

        internal void Add(string Index, string Name) // add ned column
        {
            ColumnModel col = Controller.AddColumn(UM.Email, Int32.Parse(Index), Name);
            Columns.Insert(col.Index, col);
            int i = 0;
            foreach (ColumnModel cm in Columns)
            {
                cm.Index = i;
                i++;
            }
            RaisePropertyChanged("columns");
        }


        internal void AdvanceTask(int columnIndex, int ID, TaskModel T)
        {
            Controller.AdvanceTask(UM.Email, columnIndex, ID);
            Columns[columnIndex].Tasks.Remove(T);
            Columns[columnIndex].Reload();
            Columns[columnIndex + 1].Tasks.Add(T);
            Columns[columnIndex + 1].Reload();
            Sort();
            RaisePropertyChanged("columns");
        }

        internal void Filter(string filter) // filter tasks by given string
        {
            foreach (ColumnModel c in Columns)
            {
                c.Filter(filter);
            }
        }

        internal void Unsort() // undo sorting
        {
            foreach (ColumnModel c in Columns)
            {
                c.IsSorted = false;
            }
            ReLoad();
        }
    }
}
