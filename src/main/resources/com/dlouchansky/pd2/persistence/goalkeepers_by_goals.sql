select
    p.id id,
    p.firstName firstName,
    p.lastName lastName,
    t.name team,
    t.id teamId,

    count(distinct gop.id) goalCount,
    count(distinct gap.id) gameCount,
    count(distinct gop.id)/count(distinct gap.id) ratio
from players p
left join teams t on t.id = p.teams_id
left join goalPlayers gop on (p.id = gop.players_id and gop.role = 2)
join gamePlayers gap on (p.id = gap.players_id and gap.duration > 0) /* not left join to filter players that played in no games*/
where p.role = 0
group by p.id
order by ratio asc, lastName asc
limit #LIMIT#