using KanbanUI.ViewModel;
using System.Windows;

namespace KanbanUI.View
{
    /// <summary>
    /// Interaction logic for Window1.xaml
    /// </summary>
    public partial class RegisterWindow : Window 
    {
        private readonly RegisterViewModel RVM;

        public RegisterWindow(BackendController controller)
        {
            InitializeComponent();
            RVM = new RegisterViewModel(controller);
            DataContext = RVM;
        }

        private void Register_Click(object sender, RoutedEventArgs e)
        {
            RVM.Register();
        }

        private void Back_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
    }
}
