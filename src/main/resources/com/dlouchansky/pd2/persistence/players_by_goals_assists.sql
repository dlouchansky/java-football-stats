select
    p.id id,
    p.firstName firstName,
    p.lastName lastName,
    t.name team,
    gp.role role,
    count(distinct gp.id) goalsScored,
    gp2.role role2,
    count(distinct gp2.id) goalsAssisted
from players p
left join teams t on p.teams_id = t.id
left join goalPlayers gp on (p.id = gp.players_id and gp.role = 0)
left join goalPlayers gp2 on (p.id = gp2.players_id and gp2.role = 1)
group by p.id, gp.role
order by goalsScored desc, goalsAssisted desc, lastName asc
limit #LIMIT#