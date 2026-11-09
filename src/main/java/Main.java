
import Model.Product;
import Model.TransactionIsolationLevel;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.math.BigDecimal;
import java.sql.Connection;

public class Main {

    public static void main(String[] args) {

        testIsolationLevel(TransactionIsolationLevel.READ_UNCOMMITTED);

        testIsolationLevel(TransactionIsolationLevel.READ_COMMITTED);

        testIsolationLevel(TransactionIsolationLevel.REPEATABLE_READ);

        testIsolationLevel(TransactionIsolationLevel.SERIALIZABLE);

        HibernateUtil.shutdown();
    }

    public static void testIsolationLevel(
            TransactionIsolationLevel isolationLevel) {

        Session session = null;
        Transaction transaction = null;

        try {

            session = HibernateUtil
                    .getSessionFactory()
                    .openSession();

            Connection connection =
                    session.doReturningWork(conn -> conn);

            switch (isolationLevel) {

                case READ_UNCOMMITTED:
                    connection.setTransactionIsolation(
                            Connection.TRANSACTION_READ_UNCOMMITTED);
                    break;

                case READ_COMMITTED:
                    connection.setTransactionIsolation(
                            Connection.TRANSACTION_READ_COMMITTED);
                    break;

                case REPEATABLE_READ:
                    connection.setTransactionIsolation(
                            Connection.TRANSACTION_REPEATABLE_READ);
                    break;

                case SERIALIZABLE:
                    connection.setTransactionIsolation(
                            Connection.TRANSACTION_SERIALIZABLE);
                    break;
            }

            transaction = session.beginTransaction();

            Product product =
                    session.get(Product.class, 1L);

            if (product != null) {

                product.setPrice(
                        product.getPrice().add(
                                new BigDecimal("100")));

                session.merge(product);

                System.out.println(
                        "Updated with isolation level: "
                                + isolationLevel);
            }

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            e.printStackTrace();

        } finally {

            if (session != null) {
                session.close();
            }
        }
    }
}
