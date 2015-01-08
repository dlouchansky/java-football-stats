select
    p.id,
    p.firstName firstName,
    p.lastName lastName,
    t.name team,
    t.id teamId,
    p.number number,
    coalesce(sum(gap.duration), 0) seconds
from players p
left join teams t on p.teams_id = t.id
left join gamePlayers gap on (p.id = gap.players_id)
group by p.id
order by seconds desc, p.lastName desc
limit #LIMIT#