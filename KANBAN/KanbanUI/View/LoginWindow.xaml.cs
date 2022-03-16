using KanbanUI.Model;
using KanbanUI.ViewModel;
using System.Windows;
using System.Windows.Controls;

namespace KanbanUI.View
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class LoginWindow : Window  
    {
        private readonly LoginViewModel LM;
        public LoginWindow() // empty constructor to run at startup
        {
            InitializeComponent();
            LM = new LoginViewModel();
            DataContext = LM;
        }
        public LoginWindow(BackendController controller) // post logout constructor to recycle controller
        {
            InitializeComponent();
            LM = new LoginViewModel(controller);
            DataContext = LM;
        }

        private void Login_Click(object sender, RoutedEventArgs e)
        {
            UserModel u = LM.Login();
            if (u != null)
            {
                BoardWindow boardView = new BoardWindow(u)
                {
                    WindowStartupLocation = WindowStartupLocation.CenterScreen,
                    WindowState = WindowState.Maximized
                };
                boardView.Show();
                this.Close();
            }
        }
        private void Register_Click(object sender, RoutedEventArgs e)
        {
            RegisterWindow reg = new RegisterWindow(LM.Controller)
            {
                WindowStartupLocation = WindowStartupLocation.CenterScreen
            };
            reg.ShowDialog();
        }

        // reset button to delete all data. not part of final build but in code for testing

        //private void Reset_click(object sender, RoutedEventArgs e)
        //{
        //    LM.Reset();
        //}
    }
}
