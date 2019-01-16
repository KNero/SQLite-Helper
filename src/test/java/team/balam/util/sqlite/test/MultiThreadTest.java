package team.balam.util.sqlite.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import team.balam.util.sqlite.connection.DatabaseLoader;
import team.balam.util.sqlite.connection.vo.QueryVo;
import team.balam.util.sqlite.connection.vo.QueryVoFactory;
import team.balam.util.sqlite.connection.vo.Result;

import java.util.concurrent.CountDownLatch;

public class MultiThreadTest {
    private static final String DB_NAME = "./db/test-multiThread.db";
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS TEST(DATA1 TEXT, DATA2 NUMERIC, DATA3 INTEGER, PRIMARY KEY (DATA1, DATA2))";
    private static final String DELETE_QUERY = "DELETE FROM TEST";
    private static final String INSERT_QUERY = "insert into test (data1, data2, data3) values (?, ?, ?)";

    @BeforeClass
    public static void init() throws Exception {
        DatabaseLoader.load(DB_NAME, DB_NAME);

        QueryVo vo = QueryVoFactory.create(QueryVo.Type.EXECUTE);
        vo.setQuery(CREATE_TABLE_QUERY);
        vo.execute();

        if (!vo.getResult().isSuccess()) {
            throw vo.getResult().getException();
        }

        vo = QueryVoFactory.create(QueryVo.Type.DELETE);
        vo.setQuery(DELETE_QUERY);
        vo.execute();

        if (!vo.getResult().isSuccess()) {
            throw vo.getResult().getException();
        }
    }

    @Test
    public void test() throws Exception {
        int thread = 20;
        final int insertCount = 100;
        final CountDownLatch countDownLatch = new CountDownLatch(thread);

        for (int i = 0; i < thread; ++i) {
            Thread t = new Thread(){
                @Override
                public void run() {
                    for (int j = 0; j < insertCount; ++j) {
                        QueryVo vo = QueryVoFactory.create(QueryVo.Type.INSERT);
                        vo.setQuery(INSERT_QUERY);
                        vo.setParam(this.getName(), j, this.getName() + "-" + j);
                        vo.execute();
                    }

                    countDownLatch.countDown();
                }
            };
            t.setName("thread-" + i);
            t.start();
        }

        countDownLatch.await();

        QueryVo vo = QueryVoFactory.create(QueryVo.Type.SELECT);
        vo.setQuery("select count(*) as count from test");
        vo.execute();

        Result result = vo.getResult();
        int count = (Integer) result.getSelectResult().get(0).get("count");
        Assert.assertEquals(thread * insertCount, count);
    }
}
