<#import "layout.ftl" as u>

    <@u.layout title="Player top by scored penalties">
    <table>
        <thead>
        <tr>
            <td>Nr.</td>
            <td>Name</td>
            <td>Team</td>
            <td>Penalties</td>
        </tr>
        <thead>
        <tbody>
        <#list results as result>
            <tr>
                <td>${result.nr}</td>
                <td>${result.name}</td>
                <td>${result.team}</td>
                <td>${result.goals}</td>
            </tr>
        </#list>
        </tbody>
    </table>

</@u.layout>
