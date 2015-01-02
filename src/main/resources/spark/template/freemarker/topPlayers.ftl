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
        <#list results as result>
            <tr>
                <td>${result.nr}</td>
                <td>${result.name}</td>
                <td>${result.team}</td>
                <td>${result.goals}</td>
                <td>${result.assists}</td>
            </tr>
        </#list>
        <thead>
    </table>

</@u.layout>
