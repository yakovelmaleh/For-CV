using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using IntroSE.Kanban.Backend.BusinessLayer.BoardControl;
using IntroSE.Kanban.Backend.BusinessLayer.TaskControl;
using Moq;
using NUnit.Framework;
using N = NUnit.Framework;
using System.Collections.Generic;

namespace BoardTests
{
    [TestFixture]
    public class AdvenceTask
    {
        Board b;
        List<Column> columns;
        Mock<Column> backlog;
        Mock<Column> progress;
        Mock<Column> done;
        List<Mock<Column>> Mockcolumns;

        [SetUp]
        public void Setup()
        {
            b = new Board();
            columns = new List<Column>();
            Mockcolumns = new List<Mock<Column>>();
            backlog = new Mock<Column>();
            progress = new Mock<Column>();
            done = new Mock<Column>();
            SetColumns();
        }
        private void SetColumns()
        {
            b.columns = columns;

            backlog.Object.name = "backlog";
            progress.Object.name = "in progress";
            done.Object.name = "done";

            backlog.Object.ord = 0;
            progress.Object.ord = 1;
            done.Object.ord = 2;

            columns.Add(backlog.Object);
            columns.Add(progress.Object);
            columns.Add(done.Object);

            Mockcolumns.Add(backlog);
            Mockcolumns.Add(progress);
            Mockcolumns.Add(done);

            int i = 1;
            foreach(Column c in columns)
            {
                List<Task> l = new List<Task>();
                for (int j=0; j<10; j++)
                {
                    Mock<Task> t = new Mock<Task>();
                    t.Object.title = ""+i;
                    t.Object.Cname = c.name;
                    t.Object.ID = i;
                    t.Object.email = b.email;
                    l.Add(t.Object);
                    t.Setup(x => x.GetID()).Returns(t.Object.ID);
                    i++;
                }
                
                c.host = 1;
                c.limit = 100;
                c.tasks = l;
            }
            b.IDtask = done.Object.tasks.Count + progress.Object.tasks.Count + backlog.Object.tasks.Count;
        }


        [Test]
        [TestCase(0, 9)]
        [TestCase(0,1)]
        [TestCase(0, 2)]
        [TestCase(0, 3)]
        [TestCase(0, 4)]
        [TestCase(0, 5)]
        [TestCase(0, 6)]
        [TestCase(0, 7)]
        [TestCase(0, 8)]
        [TestCase(1, 19)]
        [TestCase(1, 11)]
        [TestCase(1, 12)]
        [TestCase(1, 14)]
        [TestCase(1, 15)]
        [TestCase(1, 16)]
        [TestCase(1, 17)]
        [TestCase(1, 18)]
        public void AdvenceTask1(int col, int task)//valid value
        {
            Mockcolumns[col+1].Setup(x => x.AddTask(new Task()));
            Mockcolumns[col].Setup(x => x.DeleteTask("test", new Task()));
            //act
            Exception e = null;
            try
            {
                b.AdvanceTask(col, task);
            }
            catch (Exception e2)
            {
                e = e2;
            }

            //assert        
            N.Assert.IsNull(e, e != null ? e.Message : "need to seccses");
        }

        [Test]
        [TestCase(15)]
        [TestCase(-15)]
        [TestCase(3)]
        [TestCase(9)]
        [TestCase(81)]
        [TestCase(-3)]
        public void AdvenceTask2(int col)//invalid column number
        {
            //arrange
            //act
            Exception e = null;
            try
            {
                b.AdvanceTask(col, 1);
            }
            catch (Exception e2)
            {
                e = e2;
            }

            //assert        
            N.Assert.IsNotNull(e, "should to  fail");
            if (e != null)
                N.Assert.AreEqual(e.Message, "Invalid column number.", "not right exeption");
        }
        [Test]
        [TestCase(1)]
        [TestCase(2)]
        [TestCase(21)]
        [TestCase(29)]
        public void AdvenceTask3(int task)//check done column
        {
            //arrange
            //act
            Exception e = null;
            try
            {
                b.AdvanceTask(2, task);
            }
            catch (Exception e2)
            {
                e = e2;
            }

            //assert        
            N.Assert.IsNotNull(e, "should to  fail");
            if (e != null)
                N.Assert.AreEqual(e.Message, "Completed tasks cannot be changed.", "not right exeption");
        }
        [Test]
        [TestCase(95)]
        [TestCase(-5)]
        [TestCase(0)]
        [TestCase(78)]
        public void AdvenceTask4(int task)//invalid task number
        {
            //arrange
            //act
            Exception e = null;
            try
            {
                b.AdvanceTask(1, task);
            }
            catch (Exception e2)
            {
                e = e2;
            }

            //assert        
            N.Assert.IsNotNull(e, "should to  fail");
            if (e != null)
                N.Assert.AreEqual(e.Message, "you entered an invalid ID.", "not right exeption");

        }
        [Test]
        [TestCase(0, 19)]
        [TestCase(0, 11)]
        [TestCase(0, 21)]
        [TestCase(0, 13)]
        [TestCase(0, 14)]
        [TestCase(0, 15)]
        [TestCase(0, 16)]
        [TestCase(0, 17)]
        [TestCase(0, 18)]
        [TestCase(1, 9)]
        [TestCase(1, 1)]
        [TestCase(1, 2)]
        [TestCase(1, 4)]
        [TestCase(1, 5)]
        [TestCase(1, 6)]
        [TestCase(1,7)]
        [TestCase(1,8)]
        public void AdvenceTask5(int col,int task)//number task does not exist in this column
        {
            //arrange
            //act
            Exception e = null;
            try
            {
                b.AdvanceTask(col, task);
            }
            catch (Exception e2)
            {
                e = e2;
            }

            //assert        
            N.Assert.IsNotNull(e, "should to  fail");
            if (e != null)
                N.Assert.AreEqual(e.Message, "task does not exist in this columm.", "not right exeption");
        }
        [Test]
        [TestCase(0, 9)]
        [TestCase(0, 1)]
        [TestCase(0, 2)]
        [TestCase(0, 3)]
        [TestCase(0, 4)]
        [TestCase(0, 5)]
        [TestCase(0, 6)]
        [TestCase(0, 7)]
        [TestCase(0, 8)]
        [TestCase(1, 19)]
        [TestCase(1, 11)]
        [TestCase(1, 12)]
        [TestCase(1, 14)]
        [TestCase(1, 15)]
        [TestCase(1, 16)]
        [TestCase(1, 17)]
        [TestCase(1, 18)]
        public void AdvenceTask6(int col, int task)//number task does not exist in this column
        {
            //arrange
            Mockcolumns[col + 1].Setup(x => x.AddTask(columns[col].GetTask(task))).Callback(()=> columns[col+1].tasks.Add(new Task()));
            Mockcolumns[col].Setup(x => x.DeleteTask("test", columns[col].GetTask(task))).Callback(() => Mockcolumns[col].Object.size = 9) ;
            //act
            b.AdvanceTask(col, task);

            

            //assert        
            N.Assert.AreEqual(11,columns[col+1].tasks.Count, "fail to add the task");
            N.Assert.AreEqual(9, columns[col].size, "fail to delete the task");
        }
    }
}
