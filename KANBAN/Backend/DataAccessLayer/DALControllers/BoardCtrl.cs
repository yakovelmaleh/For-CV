using System;
using System.Collections.Generic;
using System.Data.SQLite;
using System.IO;

namespace IntroSE.Kanban.Backend.DataAccessLayer.DALControllers
{
    class BoardCtrl // this calss is used to load the initial board list upon startup
    {
        protected static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        protected readonly string connectionString;
        protected readonly string tableName = "boards";
        public const string EmailAtt = DB._emailcolumn;
        public const string HostAtt = DB._hostcolumn;
        public const string UIDAtt = DB._uidcolumn;
        protected readonly string need = "*";
        public BoardCtrl()
        {
            string path = Path.GetFullPath(Path.Combine(Directory.GetCurrentDirectory(), "KanbanDB.db"));
            connectionString = $"Data Source={path}; Version=3;";
        }

        internal bool Save(int ID, int Host, string email)
        {
            bool fail = false;
            using (var connection = new SQLiteConnection(connectionString))
            {
                SQLiteCommand command = new SQLiteCommand(null, connection);
                int res = -1;
                try
                {
                    connection.Open();
                    command.CommandText = $"INSERT INTO {tableName} ({UIDAtt} ,{HostAtt}," +
                        $"{EmailAtt}) VALUES (@IDVal,@hostVal,@EmailVal)";
                    SQLiteParameter UIDParam = new SQLiteParameter(@"IDVal", ID);
                    SQLiteParameter hostParam = new SQLiteParameter(@"hostVal", Host);
                    SQLiteParameter EmailParam = new SQLiteParameter(@"EmailVal", email);

                    command.Parameters.Add(UIDParam);
                    command.Parameters.Add(hostParam);
                    command.Parameters.Add(EmailParam);
                    command.Prepare();
                    res = command.ExecuteNonQuery();
                }
                catch (Exception)
                {
                    fail = true;
                }
                finally
                {
                    command.Dispose();
                    connection.Close();
                    if (fail)
                    {
                        log.Error("fail to Insert to " + tableName);
                        throw new Exception("fail to Insert to " + tableName);
                    }
                }
                return res > 0;
            }
        }

        //public int FindBoard(string s)
        //{
        //    bool fail = false;
        //    List<long> result = new List<long>();
        //    using (var connection = new SQLiteConnection(connectionString))
        //    {
        //        SQLiteCommand command = new SQLiteCommand(null, connection);
        //        command.CommandText = $"SELECT {EmailAtt} FROM {tableName} WHERE { EmailAtt}= '{s}'";
        //        SQLiteDataReader dataReader = null;
        //        try
        //        {
        //            connection.Open();
        //            dataReader = command.ExecuteReader();

        //            while (dataReader.Read())
        //            {
        //                result.Add(dataReader.GetInt64(4));
        //            }
        //        }
        //        catch (Exception e)
        //        {
        //            fail = true;
        //        }
        //        finally
        //        {
        //            if (dataReader != null)
        //            {
        //                dataReader.Close();
        //            }

        //            command.Dispose();
        //            connection.Close();
        //            if (fail)
        //            {
        //                log.Error("fail to delete from " + tableName);
        //                throw new Exception("fail to delete from " + tableName);
        //            }
        //        }

        //    }
        //    if(result.Count > 1)
        //    {
        //        log.Error($"Find 2 email ({s}) with same Id or not Find ID for ");
        //        throw new Exception($"Find 2 email ({s}) with same Id or not Find ID for ");
        //    }
        //    else if (result.Count == 0)
        //    {
        //        log.Error($"Not found ID for this email: ({s})");
        //        throw new Exception($"Not found ID for this email: ({s})");
        //    }
        //    return (int)result[0];
        //}

        public List<Tuple<long, long, string>> LoadData()
        {
            bool fail = false;
            List<Tuple<long, long, string>> results = new List<Tuple<long, long, string>>();
            using (var connection = new SQLiteConnection(connectionString))
            {
                SQLiteCommand command = new SQLiteCommand(null, connection)
                {
                    CommandText = $"SELECT {need} FROM {tableName}"
                };
                SQLiteDataReader dataReader = null;
                try
                {
                    connection.Open();
                    dataReader = command.ExecuteReader();

                    while (dataReader.Read())
                    {
                        results.Add(Tuple.Create(dataReader.GetInt64(0), dataReader.GetInt64(1), dataReader.GetString(2)));
                    }
                }
                catch
                {
                    fail = true;
                }
                finally
                {
                    if (dataReader != null)
                    {
                        dataReader.Close();
                    }

                    command.Dispose();
                    connection.Close();
                    if (fail)
                    {
                        log.Error("fail to delete from " + tableName);
                        throw new Exception("fail to delete from " + tableName);
                    }
                }
                log.Debug("acsses to LoadData From DataBase.");
            }
            return results;
        }
    }
}
