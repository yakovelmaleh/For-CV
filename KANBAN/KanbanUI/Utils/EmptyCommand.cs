using System;
using System.Windows.Input;

namespace KanbanUI.Utils
{
    public class EmptyCommand : ICommand // command binding that takes no parameters
    {
        readonly Action _TargetExecuteMethod;
        readonly Func<bool> _TargetCanExecuteMethod;

        public EmptyCommand(Action executeMethod)
        {
            _TargetExecuteMethod = executeMethod;
        }

        public EmptyCommand(Action executeMethod, Func<bool> canExecuteMethod)
        {
            _TargetExecuteMethod = executeMethod;
            _TargetCanExecuteMethod = canExecuteMethod;
        }

        public void RaiseCanExecuteChanged()
        {
            CanExecuteChanged(this, EventArgs.Empty);
        }

        bool ICommand.CanExecute(object parameter)
        {

            if (_TargetCanExecuteMethod != null)
            {
                return _TargetCanExecuteMethod();
            }

            if (_TargetExecuteMethod != null)
            {
                return true;
            }

            return false;
        }

        public event EventHandler CanExecuteChanged = delegate { };

        void ICommand.Execute(object parameter)
        {
            _TargetExecuteMethod?.Invoke();
        }
    }
}
