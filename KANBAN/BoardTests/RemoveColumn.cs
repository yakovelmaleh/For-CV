using IntroSE.Kanban.Backend.BusinessLayer.BoardControl;
using IntroSE.Kanban.Backend.BusinessLayer.TaskControl;
using Moq;
using NUnit.Framework;
using System;
using System.Collections.Generic;
using N = NUnit.Framework;

namespace BoardTests
{
    [TestFixture]
    public class RemoveColumn
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

            foreach (Mock<Column> c in Mockcolumns)
            {
                c.Setup(x => x.GetAll()).Returns(new List<Task>());
                c.Setup(x => x.Delete(c.Object.ord));
                c.Setup(x => x.AddTasks(new List<Task>()));
                c.Object.host = 1;

            }
        }

        [Test]
        [TestCase("TEST")]
        [TestCase("test")]
        [TestCase("TeST")]
        [TestCase("TesT")]
        [TestCase("tEsT")]
        public void RemoveColumnTest1(string email)//valid email arguments
        {
            //arrange   
            b.cur = email;
            //act
            Exception e = null;
            try
            {
                b.RemoveColumn(0);
            }
            catch (Exception e2)
            {
                e = e2;
            }

            //assert        
            N.Assert.IsNull(e, e != null ? e.Message : "need to seccses");
        }

        [Test]
        [TestCase("asd")]
        [TestCase(null)]
        [TestCase("")]
        [TestCase("Te St")]
        [TestCase("dfdfg")]
        [TestCase("op")]
        [TestCase("tes t")]
        public void RemoveColumnTest2(string email)//invalid email arguments
        {
            //arrange   
            b.cur = email;
            //act
            Exception e = null;
            try
            {
                b.RemoveColumn(0);
            }
            catch (Exception e2)
            {
                e = e2;
            }

            //assert
            N.Assert.IsNotNull(e, "need to fail: invalid email arguments");
        }


        [Test]
        [TestCase(1)]
        [TestCase(2)]
        [TestCase(0)]
        public void RemoveColumnTest3(int ord)//valid column number arguments
        {
            //arrange   

            //act
            Exception e = null;
            try
            {
                b.RemoveColumn(ord);
            }
            catch (Exception e2)
            {
                e = e2;
            }

            //assert
            N.Assert.IsNull(e, e != null ? e.Message : "need to seccses");
        }

        [Test]
        [TestCase(-1)]
        [TestCase(-20)]
        [TestCase(20)]
        [TestCase(500)]
        [TestCase(18)]
        [TestCase(-58)]
        public void RemoveColumnTest4(int ord)//invalid column number arguments
        {
            //arrange
            //act
            Exception e = null;
            try
            {
                b.RemoveColumn(ord);
            }
            catch (Exception e2)
            {
                e = e2;
            }

            //assert
            N.Assert.IsNotNull(e, "invalid column number arguments");
        }
        [Test]
        public void RemoveColumnTest5()//invalid column number arguments
        {
            //arrange   
            columns.Remove(done.Object);
            //act
            Exception e = null;
            try
            {
                b.RemoveColumn(2);
            }
            catch (Exception e2)
            {
                e = e2;
            }

            //assert
            N.Assert.IsNotNull(e, "need to fail: invalid email arguments");
        }
        [Test]
        [TestCase(0, 2)]
        [TestCase(1, 2)]
        [TestCase(0, 1)]
        [TestCase(2, 1)]
        [TestCase(1, 0)]
        [TestCase(2, 0)]
        public void RemoveColumnTest5(int ord,int del)//test when size for columns is minimum
        {
            //arrange   
            columns.Remove(Mockcolumns[del].Object);
            //act
            Exception e = null;
            try
            {
                b.RemoveColumn(ord);
            }
            catch (Exception e2)
            {
                e = e2;
            }

            //assert
            N.Assert.IsNotNull(e, "need to fail: invalid email arguments");
        }
        [Test]
        public void RemoveColumnTest6()//test set Ordinal
        {
            //arrange   
            //act
            b.RemoveColumn(0);
            //assert
            N.Assert.AreSame(columns[1], done.Object, "set Ordinal fail");
            N.Assert.AreSame(columns[0], progress.Object, "set Ordinal fail");
        }
        [Test]
        public void RemoveColumnTest7()//test set Ordinal
        {
            //arrange   
            //act
            b.RemoveColumn(1);
            //assert
            N.Assert.AreSame(columns[1], done.Object, "set Ordinal fail");
            N.Assert.AreSame(columns[0], backlog.Object, "set Ordinal fail");
        }
        [Test]
        public void RemoveColumnTest8()//test set Ordinal
        {
            //arrange   
            //act
            b.RemoveColumn(2);
            //assert
            N.Assert.AreSame(columns[1], progress.Object, "set Ordinal fail");
            N.Assert.AreSame(columns[0], backlog.Object, "set Ordinal fail");
        }


        [Test]
        public void RemoveColumnTest9()//test set Ordinal //tasks move left
        {
            //arrange   
            Mockcolumns[1].Object.fortests = true;
            Mockcolumns[0].Object.fortests = false;
            //act
            b.RemoveColumn(2);
            //assert
            N.Assert.IsTrue(columns[1].fortests, "dont get the tasks");
            N.Assert.IsFalse(columns[0].fortests, "set Ordinal fail");
        }
        [Test]
        public void RemoveColumnTest10()//test set Ordinal //tasks move left
        {
            //arrange   
            Mockcolumns[0].Object.fortests = true;
            Mockcolumns[2].Object.fortests = false;
            //act
            b.RemoveColumn(1);
            //assert
            N.Assert.IsTrue(columns[0].fortests, "dont gdt the tasks");
            N.Assert.IsFalse(columns[1].fortests, "set Ordinal fail");
        }
        [Test]
        public void RemoveColumnTest11()//test set Ordinal // test move right
        {
            //arrange  
            Mockcolumns[1].Object.fortests = true;
            Mockcolumns[2].Object.fortests = false;
            //act
            b.RemoveColumn(0);
            //assert
            N.Assert.IsTrue(columns[0].fortests, "dont gdt the tasks");
            N.Assert.IsFalse(columns[1].fortests, "set Ordinal fail");
        }
    }


}
