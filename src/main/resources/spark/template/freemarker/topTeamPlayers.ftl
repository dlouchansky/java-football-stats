<#import "layout.ftl" as u>
    <@u.layout title="Player stats for team ${team}">
    <table>
        <thead>
        <tr>
            <td>Number</td>
            <td>Name</td>
            <td>Games played</td>
            <td>Minutes</td>
            <td>Goals</td>
            <td>Assists</td>
            <td>Yellow cards</td>
            <td>Red cards</td>
        </tr>
        <#list players as result>
            <tr>
                <td>${result.number}</td>
                <td>${result.name}</td>
                <td>${result.gamesPlayed}</td>
                <td>${result.minutes}</td>
                <td>${result.goals}</td>
                <td>${result.assists}</td>
                <td>${result.yellowCards}</td>
                <td>${result.redCards}</td>
            </tr>
        </#list>
        <thead>
    </table>

    <table>
        <thead>
        <tr>
            <td>Number</td>
            <td>Name</td>
            <td>Missed goals</td>
            <td>Average missed goals</td>s
        </tr>
        <#list goalkeepers as result>
            <tr>
                <td>${result.number}</td>
                <td>${result.name}</td>
                <td>${result.missedGoals}</td>
                <td>${result.missedGoalsAverage?string["0.#"]}</td>
            </tr>
        </#list>
        <thead>
    </table>
    </@u.layout>
