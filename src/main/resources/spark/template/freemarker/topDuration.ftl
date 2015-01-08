<#import "layout.ftl" as u>

    <@u.layout title="Player top by duration">
    <table>
        <thead>
        <tr>
            <td>Nr.</td>
            <td>Name</td>
            <td>Team</td>
            <td>Duration</td>
        </tr>
        <thead>
        <tbody>
        <#list results as result>
            <tr>
                <td>${result.nr}</td>
                <td>${result.name}</td>
                <td>${result.team}</td>
                <td>${result.minutes}</td>
            </tr>
        </#list>
        </tbody>
    </table>

</@u.layout>
