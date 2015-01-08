select
    p.id id,
    p.firstName firstName,
    p.lastName lastName,
    t.name team,
    count(distinct g.id) gameCount,
    count(distinct gop.id) goalId,
    count(distinct gop.id)/count(distinct g.id) ratio,
    p.number number
from players p
left join teams t on t.id = p.teams_id
left join gamePlayers gap on p.id = gap.players_id
left join games g on (g.id = gap.games_id)
left join goals go on (g.id = go.games_id)
left join goalPlayers gop on (p.id = gop.players_id and go.id = gop.goals_id)
where p.role = 0
and g.id is not null
and t.id = #TEAMID#
group by p.id
order by number asc