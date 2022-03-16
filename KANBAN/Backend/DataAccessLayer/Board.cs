using System.Collections.Generic;

namespace IntroSE.Kanban.Backend.DataAccessLayer
{
    class Board // mainly just a small transitional class to load the tasks and columns
    {
        public List<Column> Columns { get; set; }
        public int Host { get; set; }
        public Board(int Host)
        {
            this.Host = Host;
        }

        public void LoadData()
        {
            Column temp = new Column();
            Columns = temp.GetAllColumns(Host);
        }

    }
}
