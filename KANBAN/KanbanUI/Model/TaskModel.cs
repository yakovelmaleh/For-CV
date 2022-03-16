using System;
using System.Windows.Media;

namespace KanbanUI.Model
{
    public class TaskModel : NotifiableModelObject
    {
        public bool IsAssignee { get; set; }
        private const double Orange = 0.75;
        public SolidColorBrush BackgroundBrush
        {
            get => _background;
            set
            {
                _background = value;
                RaisePropertyChanged("BackgroundBrush");
            }
        }
        private SolidColorBrush _background;
        public SolidColorBrush FilterBrush
        {
            get => _filterBrush;
            set
            {
                _filterBrush = value;
                RaisePropertyChanged("FilterBrush");
            }
        }
        private SolidColorBrush _filterBrush;
        public SolidColorBrush BorderBrush
        {
            get => _borderBrush;
            set
            {
                _borderBrush = value;
                RaisePropertyChanged("BorderBrush");
            }
        }
        private SolidColorBrush _borderBrush;
        public int ID { get; set; }
        public int ColumnIndex { get; set; }
        public string Assignee { get; set; }
        public string Title { get; set; }
        public string Desc { get; set; }
        public string Due { get => _due.ToShortDateString(); set { _due = DateTime.Parse(value); } }
        private DateTime _due;
        public DateTime Cre { get; set; }
        public TaskModel(BackendController c, int ID, string A, string T, string D, DateTime DU, DateTime C, int columnIndex, string email) : base(c) 
        { // full constructor that loads a preexisting task to presentation
            this.ID = ID;
            ColumnIndex = columnIndex;
            Assignee = A;
            Title = T;
            Desc = D;
            _due = DU;
            Cre = C;
            IsAssignee = (email == Assignee);
            // colour logic
            if (DateTime.Now > _due)
            {
                BackgroundBrush = Brushes.Crimson;
            }
            else if (((double)DateTime.Now.Subtract(Cre).Ticks / _due.Subtract(Cre).Ticks) > Orange)
            {
                BackgroundBrush = Brushes.Coral;
            }
            if (IsAssignee)
            {
                BorderBrush = Brushes.RoyalBlue;
            }
        }
        public TaskModel(BackendController c) : base(c) //empty constructor for on-the-fly task creation
        {

        }

        internal void AddTask(string title, string desc, DateTime due, string email)
        {
            if (string.IsNullOrWhiteSpace(Title))
            {
                Controller.AddTask(email, title, desc, due);
            }
        }

        internal void EditTask(string title, string desc, DateTime due, string assignee, string email) // all edits commit at once if needed
        {
            if (!title.Equals(Title)) Controller.EditTitle(email, ColumnIndex, ID, title);
            if (desc != Desc) Controller.EditDesc(email, ColumnIndex, ID, desc);
            if (!due.Equals(_due)) Controller.EditDue(email, ColumnIndex, ID, due);
            if (!assignee.Equals(Assignee)) Controller.EditAssignee(email, ColumnIndex, ID, assignee);
        }

        internal void Filter(string filter) // colour task if matching filter (case insensitive)
        {
            if (!string.IsNullOrEmpty(filter)) filter = filter.ToLower();
            if (!string.IsNullOrEmpty(filter) && (Title.ToLower().Contains(filter) || (Desc != null && Desc.ToLower().Contains(filter))))
            {
                FilterBrush = Brushes.Gold;
            }
            else
            {
                FilterBrush = BackgroundBrush;
            }
        }
    }
}
