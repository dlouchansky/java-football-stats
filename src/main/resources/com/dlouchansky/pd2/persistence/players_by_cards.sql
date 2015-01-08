select
    p.id,
    p.firstName,
    p.lastName,
    t.name,
    t.id teamId,
    count(distinct gc.id) cards
from players p
left join teams t on p.teams_id = t.id
left join gameCards gc on p.id = gc.players_id
group by p.id
order by cards desc, p.lastName asc