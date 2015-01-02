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
            <td>Goals cored</td>
            <td>Goals missed</td>
            <td>Player stats</td>
        </tr>
        <thead>
        <tbody>
        <#list results as result>
            <tr>
                <td>${result.nr}</td>
                <td>${result.title}</td>
                <td>${result.points}</td>
                <td>${result.winsMain}</td>
                <td>${result.lossesMain}</td>
                <td>${result.winsExtra}</td>
                <td>${result.lossesExtra}</td>
                <td>${result.goalsScored}</td>
                <td>${result.goalsMissed}</td>
                <td><a href="/topTeamPlayers/${result.id}">Here</a></td>
            </tr>
        </#list>
        </tbody>
    </table>

</@u.layout>
