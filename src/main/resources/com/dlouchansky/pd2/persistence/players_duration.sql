select
    p.id,
    p.number number,
    coalesce(sum(gap.duration), 0) seconds
from players p
left join teams t on p.teams_id = t.id
left join gamePlayers gap on (p.id = gap.players_id)
where t.id = #TEAMID#
group by p.id
order by number asc;