using System;
using System.Collections.Generic;
using System.Linq;
using DC = IntroSE.Kanban.Backend.DataAccessLayer.DALControllers;
using TC = IntroSE.Kanban.Backend.BusinessLayer.TaskControl;

namespace IntroSE.Kanban.Backend.BusinessLayer.BoardControl
{
    class BoardController
    {
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        private Dictionary<string, Board> BC;
        private Dictionary<string, int> hosts;
        private Dictionary<int, string> IdToEmail;
        private Board Cur;
        private string CurEmail;
        private readonly DC.BoardCtrl DBC;
        private readonly UBlink lnk;

        public BoardController(UBlink lnk)
        {
            this.lnk = lnk;
            BC = new Dictionary<string, Board>();
            hosts = new Dictionary<string, int>();
            IdToEmail = new Dictionary<int, string>();
            Cur = null;
            CurEmail = null;
            DBC = new DC.BoardCtrl();
            log.Debug("BoardController created.");
        }
        public string GetHost() { return Cur.GetEmail(); }

        public void LoadData() // load board dictionary (boards keyd by email) of all saved boards
        {
            if (lnk.Load) { log.Warn("Data already loaded."); throw new Exception("Data already loaded."); }
            List<Tuple<long, long, string>> temp = DBC.LoadData();
            temp.ForEach(x => { IdToEmail.Add((int)x.Item1, x.Item3); });
            temp.ForEach(x => { hosts.Add(x.Item3, (int)x.Item2); });
            temp.Where(x => x.Item1 == x.Item2).ToList().ForEach(x => { BC.Add(x.Item3, new Board(x.Item3, (int)x.Item1)); });
            temp.Where(x => x.Item1 != x.Item2).ToList().ForEach(x => { BC[IdToEmail[(int)x.Item2]].Join(x.Item3); });
            log.Debug("board list has been loaded.");
            lnk.Load = true;
        }
        public void Register(string email)
        {
            email = email.ToLower();
            int ID = lnk.LastId;
            string emailcheck = lnk.Lastemail;
            ResetLnk();
            if (!email.Equals(emailcheck) | ID == -1 | ExistBoardForThisID(ID))
            {
                log.Warn($"for this id #{ID} already exist a Board.");
                throw new Exception($"for this id #{ID} already exist a Board.");
            }
            IdToEmail.Add(ID, email);
            hosts.Add(email, ID);
            BC.Add(email, new Board(email, ID));
            BC[email].Register();
            CheckSave(DBC.Save(ID, ID, email));
            log.Debug($"the Board of {email} is ready.");
        }
        private void CheckSave(bool b)
        {
            if (!b)
            {
                log.Warn("fail to insert the new Register.");
                throw new Exception("fail to insert the new Register.");
            }
        }
        public void ResetLnk()
        {
            lnk.LastId = -1;
            lnk.Lastemail = null;
            lnk.HostId = -1;
        }
        public void Register(string email, string emailhost)
        {
            email = email.ToLower();
            emailhost = emailhost.ToLower();
            int ID = lnk.LastId;
            int HostID = lnk.HostId;
            string emailcheck = lnk.Lastemail;
            ResetLnk();
            if (!email.Equals(emailcheck) | ID == -1 || ExistBoardForThisID(ID))
            {
                log.Warn($"for this id #{ID} already exist a Board.");
                throw new Exception($"for this id #{ID} already exist a Board.");
            }

            IdToEmail.Add(ID, email);
            if (email.Equals(emailhost))
            {
                log.Info($"{email} try to join to his Board({email} could to Register with right fanction)");
                hosts.Add(email, ID);
                BC.Add(email, new Board(email, ID));
                BC[email].Register();
            }
            else
            {
                int IDhost = CheckHost(HostID);
                lnk.HostId = -1;
                if (IDhost != hosts[emailhost])
                {
                    log.Warn($"user with email #{emailhost} is not host Board so {email} can not to join him.");
                    throw new Exception($"user with email #{emailhost} is not host Board so {email} can not to join him.");
                }
                if (!ExistBoardForThisID(ID))
                {
                    log.Warn($"user with email #{emailhost} no host of Board.");
                    throw new Exception($"for this email #{emailhost} already exist a Board.");
                }
                hosts.Add(email, IDhost);
                BC.Add(email, BC[emailhost]);
                BC[emailhost].Join(email);
            }
            CheckSave(DBC.Save(ID, HostID, email));
            log.Debug($"the Board of {email} is ready and his Host is {emailhost}.");
        }


        private bool ExistBoardForThisID(int id)
        {
            if (!IdToEmail.ContainsKey(id)) { return false; }
            return true;
        }
        private int CheckHost(int Id)
        {
            if (Id == -1)
            {
                log.Warn($"the User try to join to userID {Id} board but is email is illegal.");
                throw new Exception($"the User try to join to userID {Id} board but is email is illegal.");
            }
            return Id;
        }

        public void Login(string email) // log in currend board holder
        {
            IsActive();
            email = email.ToLower();
            if (!hosts.ContainsKey(email))
            {
                log.Warn($"{email} try to login to system but he is not register to the system.");
                throw new Exception($"{email} try to login to system but he is not register to the system.");
            }
            log.Debug("successfully opened board for " + email + ".");
            String h = IdToEmail[hosts[email]];
            Cur = BC[h];
            CurEmail = email;
            BC[h].Login(email);
        }

        public void Logout(string email) // log out current board holder
        {
            CheckEmail(email);
            Cur.Logout();
            Cur = null;
            CurEmail = null;
            log.Debug(email + " has logged out.");
        }

        public void LimitColumnTask(string email, int ColumnOrdinal, int limit) // change the limit of a specific column
        {
            CheckEmail(email);
            Cur.LimitColumnTask(ColumnOrdinal, limit);
        }

        private void CheckEmail(string email) // checks that the email given in the request matches to the email of the currently logged in board holder
        {
            if (email == null)
            {
                log.Error("An offline user tried to take action.");
                throw new Exception("you need to login to system.");
            }
            string s = email.ToLower();
            if (Cur == null || !s.Equals(CurEmail))
            {
                log.Warn(email + " does not match the email connected to the system.");
                throw new Exception("The email you entered does not match the email connected to the system.");
            }
        }
        public TC.Task AddTask(string email, string title, string desciption, DateTime dueTime) // add a new task for this user
        {
            CheckEmail(email);
            return Cur.AddTask(title, desciption, dueTime);
        }
        public void UpdateTaskDueDate(string email, int columnOrdinal, int taskID, DateTime Due) // update due date of this task
        {
            CheckEmail(email);
            Cur.UpdateTaskDueDate(columnOrdinal, taskID, Due);
        }
        public void UpdateTaskTitle(string email, int columnOrdinal, int taskID, string title) // update title of this task
        {
            CheckEmail(email);
            Cur.UpdateTaskTitle(columnOrdinal, taskID, title);
        }
        public void UpdateTaskDescription(string email, int columnOrdinal, int taskID, string description) // update description of this task
        {
            CheckEmail(email);
            Cur.UpdateTaskDescription(columnOrdinal, taskID, description);
        }
        public void AdvanceTask(string email, int columnOrdinal, int taskId) // advance this task to the next column
        {
            CheckEmail(email);
            Cur.AdvanceTask(columnOrdinal, taskId);
        }
        public TC.Column GetColumn(string email, string columnName) // get column data of a specific column (by name)
        {
            CheckEmail(email);
            return Cur.GetColumn(columnName);
        }
        public TC.Column GetColumn(string email, int columnOrdinal) // get column data of a specific column (by ID)
        {
            CheckEmail(email);
            return Cur.GetColumn(columnOrdinal);
        }
        public List<TC.Column> GetColumns(string email) // get all columns of current board holder
        {
            CheckEmail(email);
            return Cur.GetColumns();
        }
        public Tuple<List<TC.Column>, string> GetBoard(string email)
        {
            CheckEmail(email);
            return Cur.GetBoard();
        }
        private void IsActive()
        {
            if (Cur != null)
            {
                log.Error("tried to login or drop data while a user is already logged in.");
                throw new Exception("tried to login or drop data while a user is already logged in.");
            }
        }

        public void RemoveColumn(string email, int columnOrdinal)
        {
            CheckEmail(email);
            Cur.RemoveColumn(columnOrdinal);
        }

        public TC.Column AddColumn(string email, int columnOrdinal, string Name)
        {
            CheckEmail(email);
            return Cur.AddColumn(columnOrdinal, Name);
        }

        public TC.Column MoveColumnRight(string email, int columnOrdinal)
        {
            CheckEmail(email);
            return Cur.MoveColumnRight(columnOrdinal);
        }

        public TC.Column MoveColumnLeft(string email, int columnOrdinal)
        {
            CheckEmail(email);
            return Cur.MoveColumnLeft(columnOrdinal);
        }
        public void Drop()
        {
            IsActive();
            BC = new Dictionary<string, Board>();
            IdToEmail = new Dictionary<int, string>();
            hosts = new Dictionary<string, int>();
        }
        public void DeleteTask(string email, int columnOrdinal, int taskId)
        {
            CheckEmail(email);
            Cur.DeleteTask(columnOrdinal, taskId);
        }
        public void AssignTask(string email, int columnOrdinal, int taskId, string emailAssignee)
        {
            CheckEmail(email);
            Cur.AssignTask(columnOrdinal, taskId, emailAssignee);
        }
        public void ChangeColumnName(string email, int columnOrdinal, string newName)
        {
            log.Debug("in BoardController with " + email + " and " + this.CurEmail);
            CheckEmail(email);
            Cur.ChangeColumnName(columnOrdinal, newName);
        }


    }
}
