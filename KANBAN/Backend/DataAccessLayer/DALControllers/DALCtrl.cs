using System;
using System.Collections.Generic;
using System.Data.SQLite;
using System.IO;
using DAL = IntroSE.Kanban.Backend.DataAccessLayer;

namespace IntroSE.Kanban.Backend.DataAccessLayer.DALControllers
{
    public abstract class DALCtrl<T> where T : DalObject<T>
    {
        protected static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        protected readonly string connectionString;
        protected readonly string tableName;
        protected readonly string DB = DAL.DB._databasename;
        public DALCtrl(string tableName) // constructor for each table
        {
            this.tableName = tableName;
            string path = Path.GetFullPath(Path.Combine(Directory.GetCurrentDirectory(), DB));
            this.connectionString = $"Data Source={path}; Version=3;";
        }
        public bool Delete(string Filter) // delete all entries in this table matching given filter
        {
            int res = -1;
            using (var connection = new SQLiteConnection(connectionString))
            {
                bool fail = false;
                var command = new SQLiteCommand
                {
                    Connection = connection,
                    CommandText = $"DELETE FROM {tableName} {Filter}"
                };
                try
                {
                    connection.Open();
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
                        log.Error("failed to delete from " + tableName);
                        throw new Exception("failed to delete from " + tableName);
                    }
                }
            }
            return res > 0;
        }
        public bool Update(string Filter, string attributeName, string attributeValue) //update string values
        {
            bool ex = false;
            int res = -1;
            using (var connection = new SQLiteConnection(connectionString))
            {
                SQLiteCommand command = new SQLiteCommand
                {
                    Connection = connection,
                    CommandText = $"UPDATE {tableName} SET [{attributeName}]=@{attributeName} {Filter}"
                };
                try
                {
                    command.Parameters.Add(new SQLiteParameter(attributeName, attributeValue));
                    connection.Open();
                    res = command.ExecuteNonQuery();
                }
                catch
                {
                    log.Error("failed to Update in " + tableName);
                    ex = true;

                }
                finally
                {
                    command.Dispose();
                    connection.Close();
                    if (ex) throw new Exception("failed to Update in " + tableName);
                }

            }
            return res > 0;
        }
        public bool Update(string Filter, string attributeName, long attributeValue) // update int values
        {
            bool ex = false;
            int res = -1;
            using (var connection = new SQLiteConnection(connectionString))
            {
                SQLiteCommand command = new SQLiteCommand
                {
                    Connection = connection,
                    CommandText = $"UPDATE {tableName} SET [{attributeName}]=@{attributeName} {Filter}"
                };
                try
                {
                    command.Parameters.Add(new SQLiteParameter(attributeName, attributeValue));
                    connection.Open();
                    res = command.ExecuteNonQuery();
                }
                catch (Exception)
                {
                    log.Error("failed to Update in " + tableName);
                    ex = true;

                }
                finally
                {
                    command.Dispose();
                    connection.Close();
                    if (ex) throw new Exception("failed to Update in " + tableName);
                }
            }
            return res > 0;
        }
        public List<T> Select(string Filter) // return all entries in this table matching given table
        {
            bool ex = false;
            List<T> results = new List<T>();
            using (var connection = new SQLiteConnection(connectionString))
            {
                SQLiteCommand command = new SQLiteCommand(null, connection)
                {
                    CommandText = $"SELECT * FROM {tableName} {Filter}"
                };
                //log.Debug(command.CommandText);
                SQLiteDataReader dataReader = null;
                try
                {
                    connection.Open();
                    dataReader = command.ExecuteReader();
                    while (dataReader.Read())
                    {
                        results.Add(ConvertReaderToObject(dataReader));
                    }
                }
                catch (Exception)
                {
                    log.Error("failed to Select from " + tableName);
                    ex = true;
                }
                finally
                {
                    if (dataReader != null)
                    {
                        dataReader.Close();
                    }

                    command.Dispose();
                    connection.Close();
                    if (ex) throw new Exception("failed to Select from " + tableName);
                }

            }
            return results;
        }
        protected abstract T ConvertReaderToObject(SQLiteDataReader reader); // convert a quary result into a DAL object
        public abstract bool Insert(T obj); // insert a new DAL object into table

    }
}
