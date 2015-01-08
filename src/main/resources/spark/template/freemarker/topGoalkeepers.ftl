<#import "layout.ftl" as u>

    <@u.layout title="Goalkeeper top by average missed goals desc">
    <table>
        <thead>
        <tr>
            <td>Nr.</td>
            <td>Name</td>
            <td>Team</td>
            <td>Missed goals</td>
            <td>Games</td>
            <td>Average missed goals</td>
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
                <td>${result.name}</td>
                <td><a href="/topTeamPlayers/${result.teamId}">${result.team}</a></td>
                <td>${result.missedGoals}</td>
                <td>${result.gameCount}</td>
                <td>${result.missedGoalsAverage?string["0.#"]}</td>
            </tr>
        </#list>
        </tbody>
    </table>

</@u.layout>
