<#import "layout.ftl" as u>

    <@u.layout title="Goalkeeper top by average missed goals desc">
    <table>
        <thead>
        <tr>
            <td>Nr.</td>
            <td>Name</td>
            <td>Team</td>
            <td>Average missed goals</td>
        </tr>
        <thead>
        <tbody>
        <#list results as result>
            <tr>
                <td>${result.nr}</td>
                <td>${result.name}</td>
                <td>${result.team}</td>
                <td>${result.missedGoalsAverage?string["0.#"]}</td>
            </tr>
        </#list>
        </tbody>
    </table>

</@u.layout>
