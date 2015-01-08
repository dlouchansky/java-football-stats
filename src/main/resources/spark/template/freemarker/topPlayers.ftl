<#import "layout.ftl" as u>

    <@u.layout title="Player top by goals and goal assists">
    <table>
        <thead>
        <tr>
            <td>Nr.</td>
            <td>Name</td>
            <td>Team</td>
            <td>Goals</td>
            <td>Assists</td>
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
                <td>${result.goals}</td>
                <td>${result.assists}</td>
            </tr>
        </#list>
        </tbody>
    </table>

</@u.layout>
