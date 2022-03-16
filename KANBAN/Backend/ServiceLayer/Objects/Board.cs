using System.Collections.Generic;

namespace IntroSE.Kanban.Backend.ServiceLayer
{
    public struct Board
    {
        public readonly IReadOnlyCollection<string> ColumnsNames;
        public readonly string emailCreator;
        internal Board(IReadOnlyCollection<string> columnsNames, string emailCreator)
        {
            this.ColumnsNames = columnsNames;
            this.emailCreator = emailCreator;
        }
        // You can add code here
        public override string ToString()
        {
            string ret = "";
            foreach (string c in ColumnsNames) { ret = ret + c + "\n"; }
            return ret;
        }
    }
}
