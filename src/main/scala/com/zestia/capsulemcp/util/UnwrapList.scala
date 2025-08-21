package com.zestia.capsulemcp.util

import com.zestia.capsulemcp.model._

trait UnwrapList[W, A] { def apply(w: W): List[A] }

object UnwrapList {
  given UnwrapList[ContactsResponse, Party] = (w: ContactsResponse) => w.parties
  given UnwrapList[OpportunitiesResponse, Opportunity] =
    (w: OpportunitiesResponse) => w.opportunities
  given UnwrapList[ProjectsResponse, Project] = (w: ProjectsResponse) => w.kases
}
