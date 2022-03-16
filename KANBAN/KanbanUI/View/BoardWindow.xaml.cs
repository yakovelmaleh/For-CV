using KanbanUI.Model;
using KanbanUI.ViewModel;
using System.Windows;
using System.Windows.Controls;

namespace KanbanUI.View
{
    /// <summary>
    /// Interaction logic for BoardWindow.xaml
    /// </summary>
    public partial class BoardWindow : Window 
    {
        public BoardViewModel BVM;
        public BoardWindow(UserModel u)
        {
            InitializeComponent();
            BVM = new BoardViewModel(u);
            DataContext = BVM;
        }

        private void Logout_Click(object sender, RoutedEventArgs e)
        {
            BVM.Logout();
            LoginWindow l = new LoginWindow();
            l.Show();
            this.Close();
        }

        private void Add_Task_Click(object sender, RoutedEventArgs e)
        {
            TaskWindow tw = new TaskWindow(BVM.UM);
            tw.ShowDialog();
            BVM.Reload();

        }

        private void Edit_Task_Click(object sender, RoutedEventArgs e)
        {
            TaskModel T = (TaskModel)((Button)sender).DataContext;
            TaskWindow TW = new TaskWindow(T, BVM.UM);
            TW.ShowDialog();
            BVM.Reload();
        }
    }
}
