select 
    grouped.refereeId id, 
    grouped.firstName firstName, 
    grouped.lastName lastName, 
    count(distinct grouped.gameId) games, 
    sum(grouped.cardCount) cards, 
    (sum(grouped.cardCount)/count(grouped.gameId)) ratio 
from (
    select 
        r.id refereeId, 
        r.firstName firstName, 
        r.lastName lastName, 
        g.id gameId, 
        count(distinct gc.id) cardCount 
    from referees as r 
    left join gameReferees gr on r.id = gr.referees_id 
    left join games g on gr.games_id = g.id 
    left join gameCards gc on g.id = gc.games_id 
    group by r.id, g.id 
    ) as grouped 
group by refereeId 
order by ratio desc, lastName asc 
limit #LIMIT#