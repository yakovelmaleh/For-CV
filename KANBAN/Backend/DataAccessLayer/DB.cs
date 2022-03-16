using System;
using System.Data.SQLite;
using System.IO;

namespace IntroSE.Kanban.Backend.DataAccessLayer
{
    class DB
    { // this class holds the majority of constants and creates the initial DB
        private static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        public const string _usertbalename = "users";
        public const string _boardtbalename = "boards";
        public const string _columntbalename = "columns";
        public const string _tasktbalename = "tasks";
        public const string _uidcolumn = "UID";
        public const string _emailcolumn = "email";
        public const string _passwordcolumn = "pw";
        public const string _nicknamecolumn = "nickname";
        public const string _hostcolumn = "Host";
        public const string _tidcolumn = "TID";
        public const string _assigneecolumn = "assignee";
        public const string _columnnamecolumn = "Cname";
        public const string _titlecolumn = "title";
        public const string _desccolumn = "description";
        public const string _duedatecolumn = "dueDate";
        public const string _createcolumn = "creationDate";
        public const string _ordinalcolumn = "Ord";
        public const string _limitcolumn = "lim";
        public const string _bidcolumn = "BDI";
        public const string _taskcounter = "taskcounter";
        public const string _columncounter = "columncounter";
        public const string _taskcountcolumn = "taskcounter";
        public const string _databasename = "KanbanDB.db";
        private readonly string connection_string;

        public DB()
        {
            string path = Path.GetFullPath(Path.Combine(Directory.GetCurrentDirectory(), _databasename));
            connection_string = $"Data Source={path};Version=3;";
        }
        public void Build() // build a new DB
        {
            bool ex = false;
            SQLiteConnection connection;
            using (connection = new SQLiteConnection(connection_string))
            {
                try
                {
                    connection.Open();
                    CreateUserTable(connection);
                    log.Debug("created Tables");
                    connection.Close();
                }
                catch (Exception)
                {
                    log.Error("Failed to create new DataBase");
                    ex = true;
                }
                finally
                {
                    if (ex) { throw new Exception("Failed to create new DataBase"); }
                    connection.Close();
                }
            }
        }


        private void CreateUserTable(SQLiteConnection connection) // build user table
        {
            string createTableQuery = $@"CREATE TABLE[{_usertbalename}]([{_uidcolumn}] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, [{_emailcolumn}] TEXT NOT NULL UNIQUE, [{_passwordcolumn}] TEXT NOT NULL,[{_nicknamecolumn}] TEXT NOT NULL,[{_hostcolumn}] INTEGER NOT NULL);" + '\n' +
                                      $@"CREATE TABLE[{_boardtbalename}]([{_uidcolumn}] INTEGER NOT NULL ,[{_hostcolumn}] INTEGER NOT NULL,[{_emailcolumn}] TEXT NOT NULL UNIQUE, FOREIGN KEY('{_uidcolumn}') REFERENCES '{_usertbalename}'('{_uidcolumn}'),FOREIGN KEY('{_hostcolumn}') REFERENCES '{_usertbalename}'('{_hostcolumn}'),FOREIGN KEY('{_emailcolumn}') REFERENCES '{_usertbalename}'('{_emailcolumn}') );" + '\n' +
                                      $@"CREATE TABLE[{_columntbalename}]([{_hostcolumn}] INTEGER NOT NULL,[{_columnnamecolumn}] TEXT NOT NULL,[{_ordinalcolumn}] INTEGER NOT NULL,[{_limitcolumn}] INTEGER NOT NULL,PRIMARY KEY(`{_hostcolumn}`,`{_columnnamecolumn}`), FOREIGN KEY('{_hostcolumn}') REFERENCES '{_usertbalename}'('{_hostcolumn}'));" + '\n' +
                                      $@"CREATE TABLE[{_tasktbalename}]([{_hostcolumn}] INTEGER NOT NULL ,[{_tidcolumn}] INTEGER NOT NULL,[{_assigneecolumn}] TEXT NOT NULL,[{_columnnamecolumn}] TEXT NOT NULL,[{_titlecolumn}] TEXT NOT NULL,[{_desccolumn}] TEXT,[{_duedatecolumn}] INTEGER NOT NULL,[{_createcolumn}] INTEGER NOT NULL, PRIMARY KEY(`{_hostcolumn}`,`{_tidcolumn}`),FOREIGN KEY('{_assigneecolumn}') REFERENCES '{_usertbalename}'('{_emailcolumn}'),FOREIGN KEY('{_hostcolumn}') REFERENCES '{_columntbalename}'('{_hostcolumn}') );";
            SQLiteCommand c = new SQLiteCommand(connection)
            {
                CommandText = createTableQuery
            };
            c.ExecuteNonQuery();
        }
        private void Drop(string s) // drop all tables and rebuild DataBase from nothing
        {
            using (var connection = new SQLiteConnection(connection_string))
            {
                bool ex = false;
                var command = new SQLiteCommand
                {
                    Connection = connection,
                    CommandText = $"DROP TABLE {s}"
                };
                try
                {
                    connection.Open();
                    command.ExecuteNonQuery();
                }
                catch (Exception)
                {
                    log.Error("fail to delete from " + s);
                    ex = true;
                }
                finally
                {
                    if (ex) { throw new Exception("fail to delete from " + s); }
                    command.Dispose();
                    connection.Close();
                }
            }
        }
        public void DropAll()
        {
            log.Debug("DB DAL");
            try
            {
                Drop(_usertbalename);
            }
            catch (Exception) { }

            try
            {
                Drop(_columntbalename);

            }
            catch (Exception) { }
            try
            {
                Drop(_tasktbalename);
            }
            catch (Exception) { }
            try
            {
                Drop(_boardtbalename);

            }
            catch { }
            Build();
        }

    }
}
