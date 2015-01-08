select
    p.id id,
    p.number number,
    p.firstName firstName,
    p.lastName lastName,
    gp.role role,
    count(distinct gp.id) goalsScored,
    gp2.role role2,
    count(distinct gp2.id) goalsAssisted,
    count(distinct gap.id) participatedInGame,
    count(distinct gap2.id) wasInGame,
    count(distinct gc.id) yellowCards,
    count(distinct gc2.id) redCards
from players p
left join teams t on p.teams_id = t.id
left join goalPlayers gp on (p.id = gp.players_id and gp.role = 0)
left join goalPlayers gp2 on (p.id = gp2.players_id and gp2.role = 1)
left join gamePlayers gap on (p.id = gap.players_id and gap.duration > 0)
left join gamePlayers gap2 on (p.id = gap2.players_id)
left join gameCards gc on(p.id = gc.players_id and gc.card = 0)
left join gameCards gc2 on(p.id = gc2.players_id and gc2.card = 1)
where t.id = #TEAMID#
group by p.id, gp.role
order by number asc;