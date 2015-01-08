<#import "layout.ftl" as u>

    <@u.layout title="Tournament top by points">
    <table>
        <thead>
        <tr>
            <td>Nr.</td>
            <td>Title</td>
            <td>Points</td>
            <td>Wins main time</td>
            <td>Losses main time</td>
            <td>Wins extra time</td>
            <td>Losses extra time</td>
            <td>Goals scored</td>
            <td>Goals missed</td>
        </tr>
        <thead>
        <tbody>
        <#list results as result>
            <tr>
                <td><span class=" badge
                <#if result.nr==1>
                badge-red
                <#elseif result.nr==2>
                badge-yellow
                <#elseif result.nr==3>
                badge-green
                </#if>
                ">${result.nr}</span></td>
                <td><a href="/topTeamPlayers/${result.id}">${result.title}</a></td>
                <td>${result.points}</td>
                <td>${result.winsMain}</td>
                <td>${result.lossesMain}</td>
                <td>${result.winsExtra}</td>
                <td>${result.lossesExtra}</td>
                <td>${result.goalsScored}</td>
                <td>${result.goalsMissed}</td>
            </tr>
        </#list>
        </tbody>
    </table>

</@u.layout>
