select
    p.id id,
    p.firstName firstName,
    p.lastName lastName,
    t.name team,
    gp.role role,
    count(distinct gp.id) penaltiesScored
from players p
left join teams t on p.teams_id = t.id
left join goalPlayers gp on (p.id = gp.players_id and gp.role = 0)
left join goals g on gp.goals_id =  g.id
where g.type = 1
group by p.id, gp.role
order by penaltiesScored desc, lastName asc
limit #LIMIT#