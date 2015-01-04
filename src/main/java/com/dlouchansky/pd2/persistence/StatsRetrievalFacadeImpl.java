package com.dlouchansky.pd2.persistence;


import com.dlouchansky.pd2.application.dtos.RefereeDTO;
import com.dlouchansky.pd2.application.dtos.TopDTO;
import com.dlouchansky.pd2.application.dtos.TopGoalkeeperDTO;
import com.dlouchansky.pd2.application.dtos.TopPlayerDTO;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class StatsRetrievalFacadeImpl implements StatsRetrievalFacade {
    private static Logger logger = LoggerFactory.getLogger(StatsRetrievalFacadeImpl.class);

    public List<RefereeDTO> getRefereesByAverageCardsPerGame(Integer limit) {
        List<RefereeDTO> results = new ArrayList<>();

        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            SQLQuery query = session.createSQLQuery(
                    "select " +
                        "grouped.refereeId id, " +
                        "grouped.firstName firstName, " +
                        "grouped.lastName lastName, " +
                        "count(grouped.gameId) games, " +
                        "sum(grouped.cardCount) cards, " +
                        "sum(grouped.cardCount)/count(grouped.gameId) ratio" +
                    "from (" +
                        "select " +
                            "r.id refereeId, " +
                            "r.firstName firstName, " +
                            "r.lastName lastName, " +
                            "g.id gameId, " +
                            "count(gc.id) cardCount " +
                        "from referees as r " +
                        "left join gameReferees gr on r.id = gr.referees_id " +
                        "left join games g on gr.games_id = g.id " +
                        "left join gameCards gc on g.id = gc.games_id " +
                        "group by r.id, g.id" +
                    ") as grouped " +
                    "group by refereeId " +
                    "order by ratio desc" +
                    "limit " + limit.toString()
            );
            List<Object[]> rows = query.list();

            int i = 1;
            for(Object[] row : rows){
                String name = row[1].toString() + " " + row[2].toString();
                Double ratio = Double.parseDouble(row[5].toString());
                results.add(new RefereeDTO(i++, name, ratio));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return results;
    }

    @Override
    public List<TopDTO> getTeamsByPoints() {
        List<TopDTO> results = new ArrayList<>();
        return results;
    }

    @Override
    public List<TopPlayerDTO> getPlayersByReceivedCards() {
        List<TopPlayerDTO> results = new ArrayList<>();

        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            SQLQuery query = session.createSQLQuery(
                    "select " +
                        "p.id, " +
                        "p.firstName, " +
                        "p.lastName, " +
                        "t.name, " +
                        "count(gc.id) cards " +
                    "from players p " +
                    "left join teams t on p.teams_id = t.id " +
                    "left join gameCards gc on p.id = gc.players_id " +
                    "group by p.id " +
                    "order by cards desc"
            );
            List<Object[]> rows = query.list();

            int i = 1;
            for(Object[] row : rows){
                String name = row[1].toString() + " " + row[2].toString();
                String team = row[3].toString();
                Integer cards = Integer.parseInt(row[4].toString());
                results.add(new TopPlayerDTO(i++, name, team, 0, 0, 0, "", 0, 0, 0, cards));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return results;
    }

    @Override
    public List<TopPlayerDTO> getPlayersByGoalsAndAssists(Integer limit) {
        List<TopPlayerDTO> results = new ArrayList<>();

        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            SQLQuery query = session.createSQLQuery(
                    "select " +
                        "p.id id, " +
                        "p.firstName firstName, " +
                        "p.lastName lastName, " +
                        "t.name team, " +
                        "gp.role role, " +
                        "count(gp.id) goalsScored, " +
                        "gp2.role role2, " +
                        "count(gp2.id) goalsAssisted " +
                    "from players p " +
                    "left join teams t on p.teams_id = t.id " +
                    "left join goalPlayers gp on (p.id = gp.players_id and gp.role = 0) " +
                    "left join goalPlayers gp2 on (p.id = gp2.players_id and gp2.role = 1) " +
                    "group by p.id, gp.role " +
                    "order by goalsScored desc, goalsAssisted desc" +
                    "limit " + limit.toString()
            );
            List<Object[]> rows = query.list();

            int i = 1;
            for(Object[] row : rows){
                String name = row[1].toString() + " " + row[2].toString();
                String team = row[3].toString();
                Integer goals = Integer.parseInt(row[5].toString());
                Integer assists = Integer.parseInt(row[7].toString());
                results.add(new TopPlayerDTO(i++, name, team, goals, assists, 0, "", 0, 0, 0, 0));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return results;
    }

    @Override
    public List<TopGoalkeeperDTO> getGoalkeepersByAverageMissedGoals(Integer limit) {
        return null;
    }

}
