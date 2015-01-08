select 
    t.id team,
    count(gop.id) goal
from teams t
left join players p on p.teams_id = t.id
left join goalPlayers gop on (p.id = gop.players_id and gop.role = 2)
left join goals g on gop.goals_id = g.id
group by t.id;