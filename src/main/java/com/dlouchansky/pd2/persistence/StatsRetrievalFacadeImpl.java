package com.dlouchansky.pd2.persistence;


import com.dlouchansky.pd2.application.dtos.RefereeDTO;
import com.dlouchansky.pd2.application.dtos.TopDTO;
import com.dlouchansky.pd2.application.dtos.TopGoalkeeperDTO;
import com.dlouchansky.pd2.application.dtos.TopPlayerDTO;
import com.dlouchansky.pd2.persistence.data.Team;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsRetrievalFacadeImpl implements StatsRetrievalFacade {
    private static Logger logger = LoggerFactory.getLogger(StatsRetrievalFacadeImpl.class);

    public List<RefereeDTO> getRefereesByAverageCardsPerGame(Integer limit) {
        List<RefereeDTO> results = new ArrayList<>();

        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String sql = getSql("referees_by_cards.sql");
            sql = sql.replace("#LIMIT#", limit.toString());
            SQLQuery query = session.createSQLQuery(sql);
            List<Object[]> rows = query.list();

            int i = 1;
            for (Object[] row : rows) {
                String name = row[1].toString() + " " + row[2].toString();
                Double ratio = Double.parseDouble(row[5].toString());
                results.add(new RefereeDTO(i++, name, ratio));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return results;
    }

    private Map<Integer, Integer> getTeamStat(String sqlPath) {
        Map<Integer, Integer> result = new HashMap<>();
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String sql = getSql(sqlPath);
            SQLQuery query = session.createSQLQuery(sql);
            List<Object[]> rows = query.list();

            for (Object[] row : rows) {
                Integer id = Integer.parseInt(row[0].toString());
                result.put(id, Integer.parseInt(row[1].toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return result;
    }

    private Map<Integer, String> getTeamNames() {
        Map<Integer, String> result = new HashMap<>();
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String sql = getSql("team_names.sql");
            SQLQuery query = session.createSQLQuery(sql);
            List<Object[]> rows = query.list();

            for (Object[] row : rows) {
                Integer id = Integer.parseInt(row[0].toString());
                result.put(id, row[1].toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return result;
    }

    @Override
    public List<TopDTO> getTeamsByPoints() {
        Map<Integer, Integer> teamGoalsScored = getTeamStat("team_goals_scored.sql");
        Map<Integer, Integer> teamGoalsMissed = getTeamStat("team_goals_missed.sql");
        Map<Integer, Integer> teamWinsMainTime = getTeamStat("team_won_main.sql");
        Map<Integer, Integer> teamLossesMainTime = getTeamStat("team_lost_main.sql");
        Map<Integer, Integer> teamWinsExtraTime = getTeamStat("team_won_extra.sql");
        Map<Integer, Integer> teamLossesExtraTime = getTeamStat("team_lost_extra.sql");
        Map<Integer, String> teamNames = getTeamNames();

        Map<Integer, Integer> teamPoints = new HashMap<>();
        teamNames.keySet().forEach(teamId -> teamPoints.put(teamId, 0));
        teamNames.keySet().forEach(teamId -> {
            if (teamWinsMainTime.get(teamId) == null)
                teamWinsMainTime.put(teamId, 0);
            if (teamWinsExtraTime.get(teamId) == null)
                teamWinsExtraTime.put(teamId, 0);
            if (teamLossesMainTime.get(teamId) == null)
                teamLossesMainTime.put(teamId, 0);
            if (teamLossesExtraTime.get(teamId) == null)
                teamLossesExtraTime.put(teamId, 0);
        });


        teamNames.keySet().forEach(teamId -> teamPoints.put(teamId, teamPoints.get(teamId) + teamWinsMainTime.get(teamId) * 5));
        teamNames.keySet().forEach(teamId -> teamPoints.put(teamId, teamPoints.get(teamId) + teamWinsExtraTime.get(teamId) * 3));
        teamNames.keySet().forEach(teamId -> teamPoints.put(teamId, teamPoints.get(teamId) + teamLossesMainTime.get(teamId)));
        teamNames.keySet().forEach(teamId -> teamPoints.put(teamId, teamPoints.get(teamId) + teamLossesExtraTime.get(teamId) * 2));

        List<TopDTO> results = new ArrayList<>();

        teamNames.keySet().forEach(teamId -> {
            String title = teamNames.get(teamId);
            Integer points = teamPoints.get(teamId);
            Integer winsMain = teamWinsMainTime.get(teamId);
            Integer lossesMain = teamLossesMainTime.get(teamId);
            Integer winsExtra = teamWinsExtraTime.get(teamId);
            Integer lossesExtra = teamLossesExtraTime.get(teamId);
            Integer goalsScored = teamGoalsScored.get(teamId);
            Integer goalsMissed = teamGoalsMissed.get(teamId);
            results.add(new TopDTO(0, teamId, title, points, winsMain, lossesMain, winsExtra, lossesExtra, goalsScored, goalsMissed));
        });

        results.sort((TopDTO o1, TopDTO o2) -> o1.getPoints() <= o2.getPoints() ? 1 : -1);

        int i = 1;
        for (TopDTO result : results) result.setNr(i++);

        return results;
    }

    @Override
    public List<TopPlayerDTO> getPlayersByReceivedCards() {
        List<TopPlayerDTO> results = new ArrayList<>();

        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String sql = getSql("players_by_cards.sql");
            SQLQuery query = session.createSQLQuery(sql);
            List<Object[]> rows = query.list();

            int i = 1;
            for (Object[] row : rows) {
                String name = row[1].toString() + " " + row[2].toString();
                String team = row[3].toString();
                Integer cards = Integer.parseInt(row[4].toString());
                if (cards > 0) // todo move to query
                    results.add(new TopPlayerDTO(i++, name, team, 0, 0, 0, 0, "", 0, 0, cards));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            String sql = getSql("players_by_goals_assists.sql");
            sql = sql.replace("#LIMIT#", limit.toString());
            SQLQuery query = session.createSQLQuery(sql);
            List<Object[]> rows = query.list();

            int i = 1;
            for (Object[] row : rows) {
                String name = row[1].toString() + " " + row[2].toString();
                String team = row[3].toString();
                Integer goals = Integer.parseInt(row[5].toString());
                Integer assists = Integer.parseInt(row[7].toString());
                results.add(new TopPlayerDTO(i++, name, team, goals, assists, 0, 0, "", 0, 0, 0));
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        List<TopGoalkeeperDTO> results = new ArrayList<>();

        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String sql = getSql("goalkeepers_by_goals.sql");
            sql = sql.replace("#LIMIT#", limit.toString());
            SQLQuery query = session.createSQLQuery(sql);
            List<Object[]> rows = query.list();

            int i = 1;
            for (Object[] row : rows) {
                String name = row[1].toString() + " " + row[2].toString();
                String team = row[3].toString();
                Double ratio = Double.parseDouble(row[6].toString());
                results.add(new TopGoalkeeperDTO(i++, name, team, ratio, 0, 0));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return results;

    }

    @Override
    public List<TopGoalkeeperDTO> getGoalkeepersForTeam(Team team) {

        List<TopGoalkeeperDTO> results = new ArrayList<>();

        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.update(team);
            String teamId = team.getId().toString();

            String sql = getSql("goalkeepers_for_team.sql");
            sql = sql.replace("#TEAMID#", teamId);
            SQLQuery query = session.createSQLQuery(sql);
            List<Object[]> rows = query.list();

            for (Object[] row : rows) {
                String name = row[1].toString() + " " + row[2].toString();
                String teamName = row[3].toString();
                Double ratio = Double.parseDouble(row[6].toString());
                Integer number = Integer.parseInt(row[7].toString());
                Integer missedGoals = Integer.parseInt(row[5].toString());
                Integer gameCount = Integer.parseInt(row[4].toString());
                results.add(new TopGoalkeeperDTO(number, name, teamName, ratio, missedGoals, gameCount));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return results;
    }

    @Override
    public List<TopPlayerDTO> getPlayersForTeam(Team team) {
        List<TopPlayerDTO> results = new ArrayList<>();
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.update(team);
            String teamId = team.getId().toString();
            String sql = getSql("players_for_team.sql");

            sql = sql.replace("#TEAMID#", teamId);
            SQLQuery query = session.createSQLQuery(sql);
            List<Object[]> rows = query.list();

            for (Object[] row : rows) {
                Integer number = Integer.parseInt(row[1].toString());
                String name = row[2].toString() + " " + row[3].toString();
                Integer goals = Integer.parseInt(row[5].toString());
                Integer assists = Integer.parseInt(row[7].toString());
                Integer participated = Integer.parseInt(row[8].toString());
                Integer wasInGame = Integer.parseInt(row[9].toString());
                Integer yellow = Integer.parseInt(row[10].toString());
                Integer red = Integer.parseInt(row[11].toString());

                results.add(new TopPlayerDTO(number, name, "", goals, assists, participated, wasInGame, "", yellow, red, 0));
            }

            String sql2 = getSql("players_duration.sql");
            sql2 = sql2.replace("#TEAMID#", teamId);
            SQLQuery queryDuration = session.createSQLQuery(sql2);
            List<Object[]> rows2 = queryDuration.list();

            int i = 0;
            for (Object[] row : rows2) {
                Integer seconds = Integer.parseInt(row[2].toString());
                TopPlayerDTO dto = results.get(i++);
                dto.setMinutes(secToMin(seconds));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return results;
    }

    @Override
    public List<TopPlayerDTO> getPlayersByDuration(Integer limit) {
        List<TopPlayerDTO> results = new ArrayList<>();

        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String sql = getSql("players_by_duration.sql");
            sql = sql.replace("#LIMIT#", limit.toString());
            SQLQuery query = session.createSQLQuery(sql);
            List<Object[]> rows = query.list();

            int i = 1;
            for (Object[] row : rows) {
                String name = row[1].toString() + " " + row[2].toString();
                String team = row[3].toString();
                String minutes = secToMin(Integer.parseInt(row[5].toString()));
                results.add(new TopPlayerDTO(i++, name, team, 0, 0, 0, 0, minutes, 0, 0, 0));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return results;
    }

    @Override
    public List<TopPlayerDTO> getPlayersByScoredPenalties(Integer limit) {
        List<TopPlayerDTO> results = new ArrayList<>();

        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String sql = getSql("players_by_penalties.sql");
            sql = sql.replace("#LIMIT#", limit.toString());
            SQLQuery query = session.createSQLQuery(sql);
            List<Object[]> rows = query.list();

            int i = 1;
            for (Object[] row : rows) {
                String name = row[1].toString() + " " + row[2].toString();
                String team = row[3].toString();
                Integer penalties = Integer.parseInt(row[5].toString());
                results.add(new TopPlayerDTO(i++, name, team, penalties, 0, 0, 0, "", 0, 0, 0));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return results;
    }

    private String getSql(String relPath) throws IOException, URISyntaxException {
        URL sql = getClass().getResource(relPath);
        Path path = Paths.get(sql.toURI());
        List<String> fileContents = Files.readAllLines(path);
        return String.join(" ", fileContents);
    }

    protected String secToMin(Integer secFull) {
        int min = secFull / 60;
        int sec = secFull % 60;
        return "" + min + ":" + (sec > 9 ? sec : "0" + sec);
    }

}
