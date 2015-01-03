<#import "layout.ftl" as u>

    <@u.layout title="Player top by cards">
    <table>
        <thead>
        <tr>
            <td>Nr.</td>
            <td>Name</td>
            <td>Team</td>
            <td>Cards</td>
        </tr>
        <thead>
        <tbody>
        <#list results as result>
            <tr>
                <td>${result.nr}</td>
                <td>${result.name}</td>
                <td>${result.team}</td>
                <td>${result.cards}</td>
            </tr>
        </#list>
        </tbody>
    </table>

</@u.layout>
