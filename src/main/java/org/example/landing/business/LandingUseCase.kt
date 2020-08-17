package org.example.landing.business

import br.com.clean.core.business.dto.Output
import br.com.clean.core.business.dto.ValueOutput
import br.com.clean.core.business.interactor.UseCase
import org.example.landing.domain.Landing

open class LandingUseCase(
        private val repo: LandingRepository
) : UseCase<Void, Landing>() {

    override fun execute(param: Void?): Output<Landing> {
        val landing = repo.doFetch()
        return ValueOutput(landing)
    }
}