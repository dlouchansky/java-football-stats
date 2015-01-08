package com.dlouchansky.pd2.persistence.daos;

import com.dlouchansky.pd2.persistence.HibernateUtil;
import com.dlouchansky.pd2.persistence.data.*;
import com.dlouchansky.pd2.persistence.data.game.*;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class ConcreteDAO {


    public static class GameDAO extends GenericDAO<Game> {
        public Game getByDate(Integer date) {
            Session session = null;
            Game object = null;
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                Criteria criteria = session.createCriteria(Game.class);
                object = (Game) criteria
                        .add(Restrictions.eq("date", date))
                        .uniqueResult();
            } catch (Exception e) {
                logger.error(e.getMessage());
            } finally {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            }
            return object;
        }
    }

    public static class GameRefereeDAO extends GenericDAO<GameReferee> {
    }

    public static class GoalDAO extends GenericDAO<Goal> {
    }

    public static class GoalPlayerDAO extends GenericDAO<GoalPlayer> {
    }

    public static class GameTeamDAO extends GenericDAO<GameTeam> {
    }

    public static class GameCardDAO extends GenericDAO<GameCard> {
        public GameCard getByPlayerAndGame(Player player, Game game) {
            Session session = null;
            GameCard object = null;
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                Criteria criteria = session.createCriteria(GameCard.class);
                object = (GameCard) criteria
                        .add(Restrictions.eq("player", player))
                        .add(Restrictions.eq("game", game))
                        .uniqueResult();
            } catch (Exception e) {
                logger.error(e.getMessage());
            } finally {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            }
            return object;
        }

    }

    public static class RefereeDAO extends GenericDAO<Referee> {
        public Referee getByName(String firstName, String lastName) {
            Session session = null;
            Referee object = null;
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                Criteria criteria = session.createCriteria(Referee.class);
                object = (Referee) criteria
                        .add(Restrictions.eq("firstName", firstName))
                        .add(Restrictions.eq("lastName", lastName))
                        .uniqueResult();
            } catch (Exception e) {
                logger.error(e.getMessage());
            } finally {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            }
            return object;
        }

    }

    public static class PlayerDAO extends GenericDAO<Player> {
        public List<Player> getByName(String firstName, String lastName) {
            Session session = null;
            List<Player> object = null;
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                Criteria criteria = session.createCriteria(Player.class);
                object = criteria
                        .add(Restrictions.eq("firstName", firstName))
                        .add(Restrictions.eq("lastName", lastName))
                        .list();
            } catch (Exception e) {
                logger.error(e.getMessage());
            } finally {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            }
            return object;
        }

    }

    public static class GamePlayerDAO extends GenericDAO<GamePlayer> {
    }

    public static class VenueDAO extends GenericDAO<Venue> {
        public Venue getByName(String name) {
            Session session = null;
            Venue object = null;
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                Criteria criteria = session.createCriteria(Venue.class);
                object = (Venue) criteria.add(Restrictions.eq("name", name))
                        .uniqueResult();
            } catch (Exception e) {
                logger.error(e.getMessage());
            } finally {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            }
            return object;
        }
    }

    public static class TeamDAO extends GenericDAO<Team> {
        public Team getByName(String name) {
            Session session = null;
            Team object = null;
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                Criteria criteria = session.createCriteria(Team.class);
                object = (Team) criteria.add(Restrictions.eq("name", name))
                        .uniqueResult();
            } catch (Exception e) {
                logger.error(e.getMessage());
            } finally {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            }
            return object;
        }
    }

    public static class TournamentDAO extends GenericDAO<Tournament> {
        public Tournament getByYear(Integer year) {
            Session session = null;
            Tournament object = null;
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                Criteria criteria = session.createCriteria(Tournament.class);
                object = (Tournament) criteria.add(Restrictions.eq("year", year))
                        .uniqueResult();
            } catch (Exception e) {
                logger.error(e.getMessage());
            } finally {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            }
            return object;
        }

    }

}
