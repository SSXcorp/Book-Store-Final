package mate.academy.bookstoreprod.repository.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreprod.exception.EntityNotFoundException;
import mate.academy.bookstoreprod.model.Book;
import mate.academy.bookstoreprod.repository.BookRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Add book failed. Book: " + book, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Book", Book.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Can't find all Books", e);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.createQuery("FROM Book WHERE id = :id", Book.class)
                    .setParameter("id", id)
                    .uniqueResult());
        } catch (Exception e) {
            throw new EntityNotFoundException("Can't find Book with id: " + id, e);
        }
    }
}
