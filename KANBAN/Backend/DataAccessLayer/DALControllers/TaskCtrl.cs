using System;
using System.Data.SQLite;
using DAL = IntroSE.Kanban.Backend.DataAccessLayer;

namespace IntroSE.Kanban.Backend.DataAccessLayer.DALControllers
{
    class TaskCtrl : DALCtrl<Task>
    {
        private const string TaskTableName = DAL.DB._tasktbalename;
        public TaskCtrl() : base(TaskTableName) { }
        public override bool Insert(Task obj) // insert given DAL task into DB
        {
            using (var connection = new SQLiteConnection(connectionString))
            {
                bool fail = false;
                SQLiteCommand command = new SQLiteCommand(null, connection);
                int res = -1;
                try
                {
                    connection.Open();
                    command.CommandText = $"INSERT INTO {tableName} ({Task.HostAtt} ,{Task.IDAtt}," +
                        $"{Task.EmailAtt},{Task.ColumnAtt},{Task.TitleAtt},{Task.DescAtt},{Task.DueAtt},{Task.createAtt}) " +
                        $"VALUES (@HostVal,@IDVal,@emailVal,@CnameVal,@TitleVal,@DescVal,@DueVal,@CreVal);";
                    SQLiteParameter hostParam = new SQLiteParameter(@"HostVal", obj.HostID);
                    SQLiteParameter emailParam = new SQLiteParameter(@"emailVal", obj.Email);
                    SQLiteParameter IDParam = new SQLiteParameter(@"IDVal", obj.ID);
                    SQLiteParameter CnameParam = new SQLiteParameter(@"CnameVal", obj.Cname);
                    SQLiteParameter TitleParam = new SQLiteParameter(@"TitleVal", obj.Title);
                    SQLiteParameter DescParam = new SQLiteParameter(@"DescVal", obj.Desc);
                    SQLiteParameter DueParam = new SQLiteParameter(@"DueVal", obj.Due);
                    SQLiteParameter CreParam = new SQLiteParameter(@"CreVal", obj.Create);

                    command.Parameters.Add(hostParam);
                    command.Parameters.Add(emailParam);
                    command.Parameters.Add(IDParam);
                    command.Parameters.Add(CnameParam);
                    command.Parameters.Add(TitleParam);
                    command.Parameters.Add(DescParam);
                    command.Parameters.Add(DueParam);
                    command.Parameters.Add(CreParam);
                    command.Prepare();
                    res = command.ExecuteNonQuery();
                }
                catch (Exception) { fail = true; }
                finally
                {
                    command.Dispose();
                    connection.Close();
                    if (fail)
                    {
                        log.Error("fail to insert a user in TaskCtrl");
                        throw new Exception("fail to insert a user in TaskCtrl");
                    }
                }
                return res > 0;
            }
        }
        protected override Task ConvertReaderToObject(SQLiteDataReader reader)
        {
            //log.Debug("in reader with task: "+reader.GetString(0)+" "+ reader.GetInt64(1) + " " + reader.GetString(2) + " " + reader.GetString(3) + " " + reader.GetValue(4));
            Task result = new Task(reader.GetInt64(0), reader.GetInt64(1), reader.GetString(2), reader.GetString(3), reader.GetString(4), reader.IsDBNull(5) ? null : reader.GetString(5), reader.GetInt64(6), reader.GetInt64(7));
            return result;
        }
    }
}
