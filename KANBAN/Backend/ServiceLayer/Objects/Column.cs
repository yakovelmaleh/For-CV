using System.Collections.Generic;

namespace IntroSE.Kanban.Backend.ServiceLayer
{
    public struct Column
    {
        public readonly IReadOnlyCollection<Task> Tasks;
        public readonly string Name;
        public readonly int Limit;
        internal Column(IReadOnlyCollection<Task> tasks, string name, int limit)
        {
            this.Tasks = tasks;
            this.Name = name;
            this.Limit = limit;
        }
        // You can add code here

        public override string ToString()
        {
            string ret = "";
            ret += "-COLUMN-\n";
            ret += "name: " + Name + "\n";
            ret += "limit: " + Limit + "\n";
            foreach (Task t in Tasks) { ret += t.ToString(); }
            return ret;
        }
    }
}
