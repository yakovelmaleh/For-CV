using IntroSE.Kanban.Backend.BusinessLayer;
using System;
using System.Collections.Generic;
using BL = IntroSE.Kanban.Backend.BusinessLayer.BoardControl;
using TC = IntroSE.Kanban.Backend.BusinessLayer.TaskControl;

namespace IntroSE.Kanban.Backend.ServiceLayer.SubService
{
    class BoardService
    {
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        private readonly BL.BoardController BC;
        public BoardService(UBlink lnk)
        {
            BC = new BL.BoardController(lnk);
            log.Debug("Board Service created.");
        }

        public Response LoadData() // load all data in board controller
        {
            log.Info("Loading Board Controller and list of boards.");
            try
            {
                BC.LoadData();
                return new Response();
            }
            catch (Exception e) { return new Response(e.Message); }
        }
        public Response Login(string email) // log a user to be the current board holder
        {
            log.Info(email + " Logging in to the board.");
            try
            {
                BC.Login(email);
                return new Response();
            }
            catch (Exception e) { return (new Response(e.Message)); }
        }
        public Response Logout(string email) // log out the current board holder
        {
            log.Info(email + " Logging out from the board.");
            try
            {
                BC.Logout(email);
                return new Response();
            }
            catch (Exception e) { return (new Response(e.Message)); }
        }
        public Response LimitColumnTask(string email, int ColumnOrdinal, int limit) // change the limit of a specific column
        {
            log.Info(email + " attempting to chenge the limit of column " + ColumnOrdinal + " to limit " + limit + ".");
            try
            {
                BC.LimitColumnTask(email, ColumnOrdinal, limit);
                return new Response();
            }
            catch (Exception e) { return (new Response(e.Message)); }
        }
        public Response<Task> AddTask(string email, string title, string desciption, DateTime dueTime) // add a new task for this user
        {
            log.Info(email + " attempting to add new task.");
            try
            {
                TC.Task task = BC.AddTask(email, title, desciption, dueTime);
                return new Response<Task>(new Task(task.GetID(), task.GetCreation(), task.GetDue(), title, desciption, task.GetEmail()));
            }
            catch (Exception e) { return (new Response<Task>(e.Message)); }
        }
        public Response UpdateTaskDueDate(string email, int columnOrdinal, int taskID, DateTime Due) // update due date of this task
        {
            log.Info(email + " attempting to Update the DueDate of task #" + taskID + ".");
            try
            {
                BC.UpdateTaskDueDate(email, columnOrdinal, taskID, Due);
                return new Response();
            }
            catch (Exception e) { return (new Response(e.Message)); }
        }
        public Response UpdateTaskTitle(string email, int columnOrdinal, int taskID, string title) // update title of this task
        {
            log.Info(email + " attempting to Update the title of task #" + taskID + ".");
            try
            {
                BC.UpdateTaskTitle(email, columnOrdinal, taskID, title);
                return new Response();
            }
            catch (Exception e) { return (new Response(e.Message)); }
        }
        public Response UpdateTaskDescription(string email, int columnOrdinal, int taskID, string Desc) // update description of this task
        {
            log.Info(email + " attempting to Update the description of task #" + taskID + ".");
            try
            {
                BC.UpdateTaskDescription(email, columnOrdinal, taskID, Desc);
                return new Response();
            }
            catch (Exception e) { return (new Response(e.Message)); }
        }
        public Response AdvanceTask(string email, int columnOrdinal, int taskId) // advance this task to the next column
        {
            log.Info(email + " attempting to advance task #" + taskId + ".");
            try
            {
                BC.AdvanceTask(email, columnOrdinal, taskId);
                return new Response();
            }
            catch (Exception e) { return (new Response(e.Message)); }
        }
        public Response<Column> GetColumn(string email, string columnName) // get column data of a specific column (by name)
        {
            log.Debug(email + " attempting to get the column " + columnName + ".");
            try
            {
                TC.Column columnBL = BC.GetColumn(email, columnName);
                return new Response<Column>(ChengeType(columnBL));
            }
            catch (Exception e) { return (new Response<Column>(e.Message)); }
        }
        public Response<Column> GetColumn(string email, int columnOrdinal) // get column data of a specific column (by ID)
        {
            log.Debug(email + " attempting to get column #" + columnOrdinal + ".");
            try
            {
                TC.Column columnBL = BC.GetColumn(email, columnOrdinal);
                return new Response<Column>(ChengeType(columnBL));
            }
            catch (Exception e) { return (new Response<Column>(e.Message)); }
        }
        public Response<Board> GetBoard(string email) // get all column names in the board
        {
            log.Debug(email + " attempting to get the board.");
            try
            {
                var temp = BC.GetBoard(email);
                List<TC.Column> listColumnBL = temp.Item1;
                List<string> listNames = new List<string>();
                foreach (TC.Column a in listColumnBL) { listNames.Add(a.GetName()); }
                return new Response<Board>(new Board(listNames, temp.Item2));
            }
            catch (Exception e) { return new Response<Board>(e.Message); }
        }
        public Response Drop()
        {
            try
            {
                BC.Drop();
                return new Response();
            }
            catch (Exception e)
            {
                return new Response(e.Message);
            }
        }

        public Response RemoveColumn(string email, int columnOrdinal)
        {
            try
            {
                log.Info(email + " attempting to remove column number #" + columnOrdinal);
                BC.RemoveColumn(email, columnOrdinal);
                return new Response();
            }
            catch (Exception e) { return new Response(e.Message); }
        }

        public Response<Column> AddColumn(string email, int columnOrdinal, string Name)
        {
            try
            {
                log.Info(email + " attempting to Add column number #" + columnOrdinal);
                Column ans = ChengeType(BC.AddColumn(email, columnOrdinal, Name));
                return new Response<Column>(ans);
            }
            catch (Exception e) { return new Response<Column>(e.Message); }
        }

        public Response<Column> MoveColumnRight(string email, int columnOrdinal)
        {
            try
            {
                log.Info(email + " attempting to move to right, column number #" + columnOrdinal);
                Column ans = ChengeType(BC.MoveColumnRight(email, columnOrdinal));
                return new Response<Column>(ans);
            }
            catch (Exception e) { return new Response<Column>(e.Message); }
        }

        public Response<Column> MoveColumnLeft(string email, int columnOrdinal)
        {
            try
            {
                log.Info(email + " attempting to move to left, column number #" + columnOrdinal);
                Column ans = ChengeType(BC.MoveColumnLeft(email, columnOrdinal));
                return new Response<Column>(ans);
            }
            catch (Exception e) { return new Response<Column>(e.Message); }
        }

        private Task ChengeType(TC.Task taskBL) // convert a BuisnessLayer task to a ServiceLayer task
        {
            return new Task(taskBL.GetID(), taskBL.GetCreation(), taskBL.GetDue(), taskBL.GetTitle(), taskBL.GetDesc(), taskBL.GetEmail());
        }
        private Column ChengeType(TC.Column columnBL)// convert a BuisnessLayer column to a ServiceLayer column
        {
            List<Task> TaskListSL = new List<Task>();
            foreach (TC.Task taskBL in columnBL.GetAll())
            { TaskListSL.Add(ChengeType(taskBL)); }
            return new Column(TaskListSL, columnBL.GetName(), columnBL.GetLimit());
        }
        public Response AssignTask(string email, int columnOrdinal, int taskId, string emailAssignee)
        {
            try
            {
                log.Info(email + " attempting to move to left, column number #" + columnOrdinal);
                BC.AssignTask(email, columnOrdinal, taskId, emailAssignee);
                return new Response();
            }
            catch (Exception e)
            {
                return new Response(e.Message);
            }
        }
        public Response DeleteTask(string email, int columnOrdinal, int taskId)
        {

            try
            {
                log.Info($"{email} attempting to Delete task #{taskId} from column #{columnOrdinal}");
                BC.DeleteTask(email, columnOrdinal, taskId);
                return new Response();
            }
            catch (Exception e)
            {
                return new Response(e.Message);
            }
        }
        public Response Register(string email, string emailhost)
        {
            try
            {
                BC.Register(email, emailhost);
                return new Response();
            }
            catch (Exception e)
            {
                return new Response(e.Message);
            }
        }
        public Response Register(string email)
        {
            try
            {
                log.Debug("attempting to create a board for new User : " + email);
                BC.Register(email);
                return new Response();
            }
            catch (Exception e)
            {
                return new Response(e.Message);
            }
        }
        public Response ChangeColumnName(string email, int columnOrdinal, string newName)
        {
            try
            {
                log.Info($"{email} attempting to chenge the name of column #{columnOrdinal} to {newName}.");
                BC.ChangeColumnName(email, columnOrdinal, newName);
                return new Response();
            }
            catch (Exception e)
            {
                return new Response(e.Message);
            }
        }
    }
}